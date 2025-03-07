package com.youllbecold.recomendation.internal

import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.logdatabase.model.Feeling
import com.youllbecold.logdatabase.model.Feelings
import com.youllbecold.recomendation.internal.model.BodyPart
import com.youllbecold.recomendation.internal.model.ClothesWeight
import com.youllbecold.recomendation.internal.model.categorizedClothesWeights
import com.youllbecold.recomendation.internal.model.clothesWeights
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.collections.plus

/**
 * Helps with outfit recommendations calculations.
 */
internal object OutfitHelper {
    /**
     * Adjusts clothes based on feelings. Returns new set of clothes adjusted per feeling.
     *
     * Note: Function uses recursion, but it should be fine performance-wise as it's limited to at most 2 calls.
     */
    suspend fun adjustPerFeeling(
        clothes: List<Clothes>,
        feeling: Feeling,
        bodyPart: BodyPart
    ): List<Clothes> = withContext(Dispatchers.Default) {
        val itemSelector = getItemSelector(bodyPart)

        val weightedCurrent: Map<Clothes, Int> = clothes
            .replaceFullBodyClothes() // Replace full body clothes with parts for better computing
            .filterAndMapWithWeights(itemSelector) // Filter out clothes with no weight and map to its weight

        // Filter only relevant clothes as potential result candidates
        val filteredList = weightedCurrent.map { it.key }

        return@withContext when (feeling) {
            Feeling.NORMAL -> filteredList
            Feeling.WARM -> adjustTooWarm(weightedCurrent, itemSelector, filteredList)
            Feeling.COLD -> adjustTooCold(weightedCurrent, itemSelector, filteredList)
            Feeling.VERY_WARM -> {
                // Just run it through warm twice
                val adjustedPerWarm = adjustTooWarm(weightedCurrent, itemSelector, filteredList)
                adjustPerFeeling(
                    adjustedPerWarm,
                    Feeling.WARM,
                    bodyPart
                )
            }
            Feeling.VERY_COLD -> {
                // Same as very warm, run it twice through cold logic.
                val adjustedPerCold = adjustTooCold(weightedCurrent, itemSelector, filteredList)
                adjustPerFeeling(
                    adjustedPerCold,
                    Feeling.COLD,
                    bodyPart
                )
            }
        }
    }

    /**
     * Calculates certainty of clothes based on feelings.
     */
    fun calculateCertainty(feelings: Feelings): Map<BodyPart, Double> = mapOf(
        BodyPart.HEAD to evaluateCertainty(feelings.head, BodyPart.HEAD.isSingleItem()),
        BodyPart.NECK to evaluateCertainty(feelings.neck, BodyPart.NECK.isSingleItem()),
        BodyPart.TOP to evaluateCertainty(feelings.top, BodyPart.TOP.isSingleItem()),
        BodyPart.BOTTOM to evaluateCertainty(feelings.bottom, BodyPart.BOTTOM.isSingleItem()),
        BodyPart.HANDS to evaluateCertainty(feelings.hand, BodyPart.HANDS.isSingleItem()),
        BodyPart.FEET to evaluateCertainty(feelings.feet, BodyPart.FEET.isSingleItem()),
    )

    private fun adjustTooWarm(
        weightedCurrent: Map<Clothes, Int>,
        itemSelector: (ClothesWeight) -> Int,
        filteredList: List<Clothes>
    ): List<Clothes> {
        val warmest = weightedCurrent.maxByOrNull { it.value }
            ?: return emptyList() // Nothing we can do

        val candidates = clothesWeights.entries.filter { entry ->
            itemSelector(entry.value) > 0 && itemSelector(entry.value) < warmest.value
                    && !weightedCurrent.contains(entry.key)
        }

        return when {
            // No lighter available, remove all
            candidates.isEmpty() && weightedCurrent.size <= 1 -> emptyList()

            candidates.isEmpty() && weightedCurrent.count() > 1 -> {
                // If there is more than one warm item, remove the lightest
                val lightest = weightedCurrent.minBy { it.value }
                filteredList.filter { it != lightest.key }
            }

            else -> {
                // Replace with the less warm available
                val warmestLighter = candidates.maxBy { itemSelector(it.value) }
                filteredList.map { if (it == warmest.key) warmestLighter.key else it }
            }
        }
    }

    private fun adjustTooCold(
        weightedCurrent: Map<Clothes, Int>,
        itemSelector: (ClothesWeight) -> Int,
        filteredList: List<Clothes>
    ): List<Clothes> {
        // Find warmer in given category. First replace wins.
        weightedCurrent.entries
            .groupBy { it.key.category }
            .forEach { currentCategory ->
                currentCategory.value.minByOrNull { it.value }?.let { lightest ->
                    val warmerInCategory =
                        categorizedClothesWeights[currentCategory.key]
                            ?.filter { itemSelector(it.value) > lightest.value }
                            ?.minByOrNull { itemSelector(it.value) }

                    if (warmerInCategory != null) {
                        // Replace with candidate in the category
                        return filteredList.map {
                            if (it == lightest.key) warmerInCategory.key else it
                        }
                    }
                }
            }

        // If there is some available category missing, add least warm one
        return clothesWeights.entries
            .filter { entry -> itemSelector(entry.value) > 0 && filteredList.none { entry.key.category == it.category } }
            .minByOrNull { itemSelector(it.value) }
            ?.let { filteredList + it.key }
            ?: filteredList // Nothing more we can do
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

    private fun getItemSelector(bodyPart: BodyPart) = when (bodyPart) {
        BodyPart.HEAD -> { weight: ClothesWeight -> weight.head }
        BodyPart.NECK -> { weight: ClothesWeight -> weight.neck }
        BodyPart.TOP -> { weight: ClothesWeight -> weight.top }
        BodyPart.BOTTOM -> { weight: ClothesWeight -> weight.bottom }
        BodyPart.HANDS -> { weight: ClothesWeight -> weight.hands }
        BodyPart.FEET -> { weight: ClothesWeight -> weight.feet }
    }

    private fun List<Clothes>.filterAndMapWithWeights(weight: (ClothesWeight) -> Int): Map<Clothes, Int> =
        this.associate { it to weight(clothesWeights[it] ?: ClothesWeight()) }
            .filter { it.value > 0 }

    private fun List<Clothes>.replaceFullBodyClothes(): List<Clothes> =
        this.flatMap {
            when (it) {
                Clothes.SLEEVELESS_SHORT_DRESS -> listOf(Clothes.TANK_TOP, Clothes.SHORT_SKIRT)
                Clothes.SLEVESLESS_LONG_DRESS -> listOf(Clothes.TANK_TOP, Clothes.LONG_SKIRT)
                Clothes.LONG_SLEEVE_SHORT_DRESS -> listOf(Clothes.LONG_SLEEVE, Clothes.SHORT_SKIRT)
                Clothes.LONG_SLEEVE_LONG_DRESS -> listOf(Clothes.LONG_SLEEVE, Clothes.LONG_SKIRT)
                Clothes.SHORT_TSHIRT_DRESS -> listOf(Clothes.SHORT_SLEEVE, Clothes.SHORT_SKIRT)
                Clothes.LONG_TSHIRT_DRESS -> listOf(Clothes.SHORT_SLEEVE, Clothes.LONG_SKIRT)
                else -> listOf(it)
            }
        }
}
