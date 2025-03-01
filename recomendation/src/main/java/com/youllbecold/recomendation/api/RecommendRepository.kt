package com.youllbecold.recomendation.api

import com.youllbecold.recomendation.model.Recommendation

/**
 * A repository for recommendations.
 */
interface RecommendRepository {
    /**
     * Recommends clothes based on the weather.
     *
     * @param hourlyApparentTemperatures The hourly apparent temperatures.
     * @param usesCelsius What unit is used for the [hourlyApparentTemperatures].
     * @param uvIndex The UV indices.
     * @param rainProbability The rain probabilities.
     *
     * @return The recommendation.
     */
    suspend fun recommend(
        hourlyApparentTemperatures: List<Double>,
        usesCelsius: Boolean,
        uvIndex: List<Double>,
        rainProbability: List<Int>
    ): Recommendation?
}
