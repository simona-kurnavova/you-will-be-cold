package com.youllbecold.recomendation.internal.data.outfit

import com.youllbecold.logdatabase.model.Clothes

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
    fun createOutfitRecommendation(minTemp: Double): List<Clothes> {
        return when {
            minTemp < 0 -> listOf(
                Clothes.WINTER_HAT,
                Clothes.LONG_SLEEVE,
                Clothes.WARM_PANTS,
                Clothes.WINTER_JACKET,
                Clothes.WINTER_SHOES,
                Clothes.GLOVES
            )
            minTemp < 10 -> listOf(
                Clothes.WINTER_HAT,
                Clothes.LONG_SLEEVE,
                Clothes.WARM_PANTS,
                Clothes.WINTER_JACKET,
                Clothes.WINTER_SHOES
            )
            minTemp < 20 -> listOf(
                Clothes.WINTER_HAT,
                Clothes.LONG_SLEEVE,
                Clothes.WARM_PANTS,
                Clothes.LIGHT_JACKET,
                Clothes.WINTER_SHOES
            )
            minTemp < 30 -> listOf(
                Clothes.WINTER_HAT,
                Clothes.SHORT_SLEEVE,
                Clothes.SHORTS,
                Clothes.LIGHT_JACKET,
                Clothes.SANDALS
            )
            else -> listOf(
                Clothes.BASEBALL_HAT,
                Clothes.SHORT_SLEEVE,
                Clothes.SHORTS,
                Clothes.SANDALS
            )
        }
    }
}
