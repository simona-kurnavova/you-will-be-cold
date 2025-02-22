package com.youllbecold.recomendation.internal

import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.logdatabase.model.Feeling
import com.youllbecold.logdatabase.model.Feelings

internal object OutfitHelper {
    private val clothesWeights: Map<Clothes, ClothesWeight> = Clothes.entries.associateWith {
        when(it) {
            Clothes.BASEBALL_HAT -> ClothesWeight(head = 1)
            Clothes.WINTER_HAT -> ClothesWeight(head = 3)

            Clothes.TANK_TOP -> ClothesWeight(top = 1)
            Clothes.SHORT_SLEEVE -> ClothesWeight(top = 2)
            Clothes.LONG_SLEEVE -> ClothesWeight(top = 3)

            Clothes.LIGHT_JACKET -> ClothesWeight(top = 3)
            Clothes.WINTER_JACKET -> ClothesWeight(top = 5)

            Clothes.SHORT_SKIRT -> ClothesWeight(bottom = 1)
            Clothes.SHORTS -> ClothesWeight(bottom = 1)
            Clothes.LONG_SKIRT -> ClothesWeight(bottom = 2)
            Clothes.LEGGINGS -> ClothesWeight(bottom = 2)
            Clothes.JEANS -> ClothesWeight(bottom = 3)
            Clothes.WARM_PANTS -> ClothesWeight(bottom = 3)

            Clothes.SANDALS -> ClothesWeight(feet = 2)
            Clothes.TENNIS_SHOES -> ClothesWeight(feet = 3)
            Clothes.WINTER_SHOES -> ClothesWeight(feet = 3)

            Clothes.TIGHTS -> ClothesWeight(hands = 1)
            Clothes.SCARF -> ClothesWeight(neck = 1)
            Clothes.GLOVES -> ClothesWeight(hands = 1)

            // Accessories and full body are never recommended.
            else -> ClothesWeight()
        }
    }

    private val categorizedClothesWeights = clothesWeights.entries.groupBy { it.key.category }

    /**
     * Adjusts clothes based on feelings. Returns new set of clothes and certainty level.
     */
    fun adjustClothes(
        clothes: List<Clothes>,
        feelings: Feelings
    ): Pair<List<Clothes>, Double> {
        val clothes = listOf(
            adjustBasedOnFeeling(clothes, feelings.head, BodyPart.HEAD),
            adjustBasedOnFeeling(clothes, feelings.neck, BodyPart.NECK),
            adjustBasedOnFeeling(clothes, feelings.top, BodyPart.TOP),
            adjustBasedOnFeeling(clothes, feelings.bottom, BodyPart.BOTTOM),
            adjustBasedOnFeeling(clothes, feelings.hand, BodyPart.HANDS),
            adjustBasedOnFeeling(clothes, feelings.feet, BodyPart.FEET)
        ).flatten().distinct()


        val certainty = listOf(
            evaluateCertainty(feelings.head, true),
            evaluateCertainty(feelings.neck, true),
            evaluateCertainty(feelings.top, false),
            evaluateCertainty(feelings.bottom, false),
            evaluateCertainty(feelings.hand, true),
            evaluateCertainty(feelings.feet, true)
        ).average()

        return clothes to certainty
    }

    private fun adjustBasedOnFeeling(
        clothes: List<Clothes>,
        feeling: Feeling,
        bodyPart: BodyPart
    ): List<Clothes> {
        val itemSelector = getItemSelector(bodyPart)
        val weightedCurrent: Map<Clothes, Int> = associateClothesWithWeight(clothes, itemSelector)
        val filteredList = weightedCurrent.map { it.key }

        if (feeling == Feeling.NORMAL) {
            return filteredList
        }

        return when (feeling) {
            Feeling.WARM -> {
                val warmest = weightedCurrent.maxByOrNull { it.value } ?: return emptyList() // Nothing we can do
                val candidates = clothesWeights.entries.filter { entry ->
                    itemSelector(entry.value) > 0 && itemSelector(entry.value) < warmest.value
                            && !weightedCurrent.contains(entry.key)
                }

                when {
                    // No lighter available, remove all
                    candidates.isEmpty() && weightedCurrent.size <= 1 -> emptyList()

                    candidates.isEmpty() && weightedCurrent.count() > 1 -> {
                        // If there is more than one warm item, remove the lightest
                        val lightest = weightedCurrent.minBy { it.value }
                        return filteredList.filter { it != lightest.key }
                    }

                    else -> {
                        // Replace with the less warm available
                        val warmestLighter = candidates.maxBy { itemSelector(it.value) }
                        filteredList.map { if (it == warmest.key) warmestLighter.key else it }
                    }
                }
            }

            Feeling.COLD -> {
                // Find warmer in given category. First replace wins.
                weightedCurrent.entries
                    .groupBy { it.key.category }
                    .forEach { currentCategory ->
                        currentCategory.value.minByOrNull { it.value }?.let { lightest ->
                            val warmerInCategory = categorizedClothesWeights[currentCategory.key]
                                ?.filter { itemSelector(it.value) > lightest.value }
                                ?.minByOrNull { itemSelector(it.value) }

                            if (warmerInCategory != null) {
                                // Replace with candidate in the category
                                return filteredList.map { if (it == lightest.key) warmerInCategory.key else it }
                            }
                        }
                    }

                // If there is some available category missing, add least warm one
                clothesWeights.entries
                    .filter { entry -> itemSelector(entry.value) > 0 && filteredList.none { entry.key.category == it.category } }
                    .minByOrNull { itemSelector(it.value) }
                    ?.let { filteredList + it.key }
                    ?: filteredList // Nothing more we can do
            }

            Feeling.VERY_WARM -> // Just run it twice through the warm logic
                adjustBasedOnFeeling(
                    adjustBasedOnFeeling(filteredList, Feeling.WARM, bodyPart),
                    Feeling.WARM,
                    bodyPart
                )

            Feeling.VERY_COLD ->
                adjustBasedOnFeeling(
                    adjustBasedOnFeeling(filteredList, Feeling.COLD, bodyPart),
                    Feeling.COLD,
                    bodyPart
                )
            else -> throw IllegalArgumentException("Unknown feeling: $feeling") // Should not happen
        }
    }

    private fun getItemSelector(bodyPart: BodyPart) = when (bodyPart) {
        BodyPart.HEAD -> { weight: ClothesWeight -> weight.head }
        BodyPart.NECK -> { weight: ClothesWeight -> weight.neck }
        BodyPart.TOP -> { weight: ClothesWeight -> weight.top }
        BodyPart.BOTTOM -> { weight: ClothesWeight -> weight.bottom }
        BodyPart.HANDS -> { weight: ClothesWeight -> weight.hands }
        BodyPart.FEET -> { weight: ClothesWeight -> weight.feet }
    }

    private fun evaluateCertainty(
        feeling: Feeling,
        isSingleItem: Boolean
    ): Double = when(feeling) {
        Feeling.NORMAL -> 1.0
        Feeling.COLD,
        Feeling.WARM -> if (isSingleItem) 0.9 else 0.8
        Feeling.VERY_COLD,
        Feeling.VERY_WARM -> if (isSingleItem) 0.6 else 0.5
    }

    private fun associateClothesWithWeight(clothes: List<Clothes>, weight: (ClothesWeight) -> Int): Map<Clothes, Int> =
        clothes
            .associate { it to weight(clothesWeights[it] ?: ClothesWeight()) }
            .filter { it.value > 0 }

    private enum class BodyPart {
        HEAD,
        NECK,
        TOP,
        BOTTOM,
        HANDS,
        FEET,
    }

    private class ClothesWeight(
        val head: Int = 0,
        val neck: Int = 0,
        val top: Int = 0,
        val bottom: Int = 0,
        val hands: Int = 0,
        val feet: Int = 0,
    )
}
