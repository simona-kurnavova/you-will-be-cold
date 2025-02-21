package com.youllbecold.recomendation.api

import com.youllbecold.recomendation.model.Recommendation

/**
 * A repository for recommendations.
 */
interface RecommendRepository {
    suspend fun recommend(
        hourlyTemperatures: List<Double>,
        hourlyApparentTemperatures: List<Double>,
        uvIndex: List<Double>,
        rainProbability: List<Int>
    ): Recommendation
}
