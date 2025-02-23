package com.youllbecold.recomendation.api

import com.youllbecold.recomendation.model.Recommendation

/**
 * A repository for recommendations.
 */
interface RecommendRepository {
    suspend fun recommend(
        hourlyApparentTemperatures: List<Double>,
        usesCelsius: Boolean,
        uvIndex: List<Double>,
        rainProbability: List<Int>
    ): Recommendation?
}
