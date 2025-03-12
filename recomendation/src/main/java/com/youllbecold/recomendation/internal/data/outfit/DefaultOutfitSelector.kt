package com.youllbecold.recomendation.internal.data.outfit

import com.youllbecold.logdatabase.model.ClothesModel

/**
 * Default outfit selector. Basically guesswork based only on min apparent temperature.
 * Serves as placeholder before we gather enough data to make more accurate recommendations.
 */
internal object DefaultOutfitSelector {
    /**
     * Creates a recommendation based on the minimum apparent temperature.
     *
     * @param minTemp The minimum apparent temperature.
     * @return The recommended clothes.
     */
    fun createOutfitRecommendation(minTemp: Double): List<ClothesModel> {
        return when {
            minTemp < 0 -> listOf(
                ClothesModel.WINTER_HAT,
                ClothesModel.LONG_SLEEVE,
                ClothesModel.WARM_PANTS,
                ClothesModel.WINTER_JACKET,
                ClothesModel.WINTER_SHOES,
                ClothesModel.GLOVES
            )
            minTemp < 10 -> listOf(
                ClothesModel.WINTER_HAT,
                ClothesModel.LONG_SLEEVE,
                ClothesModel.WARM_PANTS,
                ClothesModel.WINTER_JACKET,
                ClothesModel.WINTER_SHOES
            )
            minTemp < 20 -> listOf(
                ClothesModel.WINTER_HAT,
                ClothesModel.LONG_SLEEVE,
                ClothesModel.WARM_PANTS,
                ClothesModel.LIGHT_JACKET,
                ClothesModel.WINTER_SHOES
            )
            minTemp < 30 -> listOf(
                ClothesModel.WINTER_HAT,
                ClothesModel.SHORT_SLEEVE,
                ClothesModel.SHORTS,
                ClothesModel.LIGHT_JACKET,
                ClothesModel.SANDALS
            )
            else -> listOf(
                ClothesModel.BASEBALL_HAT,
                ClothesModel.SHORT_SLEEVE,
                ClothesModel.SHORTS,
                ClothesModel.SANDALS
            )
        }
    }
}
