package com.youllbecold.recomendation.internal.data.outfit

import com.youllbecold.logdatabase.model.ClothesModel
import com.youllbecold.logdatabase.model.Feeling
import com.youllbecold.recomendation.internal.data.utils.getItemSelector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Helps with outfit recommendations calculations.
 */
internal object OutfitProcessor {
    /**
     * Weights of clothes for different categories.
     */
    private val categorizedClothesWeights = clothesModelWeights.entries.groupBy { it.key.category }

    /**
     * Adjusts clothes based on feelings. Returns new set of clothes adjusted per feeling.
     *
     * Note: Function uses recursion, but it should be fine performance-wise as it's limited to at most 2 calls.
     */
    suspend fun adjustPerFeeling(
        clothes: List<ClothesModel>,
        feeling: Feeling,
        bodyPart: BodyPart
    ): List<ClothesModel> = withContext(Dispatchers.Default) {
        val itemSelector = bodyPart.getItemSelector()

        val weightedCurrent: Map<ClothesModel, Int> = clothes
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

    private fun adjustTooWarm(
        weightedCurrent: Map<ClothesModel, Int>,
        itemSelector: (ClothesWeight) -> Int,
        filteredList: List<ClothesModel>
    ): List<ClothesModel> {
        val warmest = weightedCurrent.maxByOrNull { it.value }
            ?: return emptyList() // Nothing we can do

        val candidates = clothesModelWeights.entries.filter { entry ->
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
        weightedCurrent: Map<ClothesModel, Int>,
        itemSelector: (ClothesWeight) -> Int,
        filteredList: List<ClothesModel>
    ): List<ClothesModel> {
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
        return clothesModelWeights.entries
            .filter { entry -> itemSelector(entry.value) > 0 && filteredList.none { entry.key.category == it.category } }
            .minByOrNull { itemSelector(it.value) }
            ?.let { filteredList + it.key }
            ?: filteredList // Nothing more we can do
    }

    private fun List<ClothesModel>.filterAndMapWithWeights(weight: (ClothesWeight) -> Int): Map<ClothesModel, Int> =
        this.associate { it to weight(clothesModelWeights[it] ?: ClothesWeight()) }
            .filter { it.value > 0 }

    private fun List<ClothesModel>.replaceFullBodyClothes(): List<ClothesModel> =
        this.flatMap {
            when (it) {
                ClothesModel.SLEEVELESS_SHORT_DRESS -> listOf(ClothesModel.TANK_TOP, ClothesModel.SHORT_SKIRT)
                ClothesModel.SLEVESLESS_LONG_DRESS -> listOf(ClothesModel.TANK_TOP, ClothesModel.LONG_SKIRT)
                ClothesModel.LONG_SLEEVE_SHORT_DRESS -> listOf(ClothesModel.LONG_SLEEVE, ClothesModel.SHORT_SKIRT)
                ClothesModel.LONG_SLEEVE_LONG_DRESS -> listOf(ClothesModel.LONG_SLEEVE, ClothesModel.LONG_SKIRT)
                ClothesModel.SHORT_TSHIRT_DRESS -> listOf(ClothesModel.SHORT_SLEEVE, ClothesModel.SHORT_SKIRT)
                ClothesModel.LONG_TSHIRT_DRESS -> listOf(ClothesModel.SHORT_SLEEVE, ClothesModel.LONG_SKIRT)
                else -> listOf(it)
            }
        }
}
