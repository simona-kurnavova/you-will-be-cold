package com.youllbecold.recomendation.internal.data.rain

import com.youllbecold.recomendation.model.RainRecommendation

/**
 * Advisor class for rain recommendations.
 */
internal object RainAdvisor {
    /**
     * Returns the rain recommendation based on the given rain probability.
     *
     * @param rainProbability The rain probabilities hourly list.
     * @return The rain recommendation.
     */
    fun rainRecommendation(rainProbability: List<Int>): RainRecommendation {
        val maxProbability = rainProbability.maxOrNull()
            ?: return RainRecommendation.NoRain

        return when {
            maxProbability < 1 -> RainRecommendation.NoRain
            maxProbability < 5 -> RainRecommendation.LightRain
            maxProbability < 8 -> RainRecommendation.MediumRain
            else -> RainRecommendation.HeavyRain
        }
    }
}
