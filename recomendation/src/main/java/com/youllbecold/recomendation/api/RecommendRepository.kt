package com.youllbecold.recomendation.api

import com.youllbecold.recomendation.model.Recommendation

/**
 * A repository for recommendations.
 */
interface RecommendRepository {
    suspend fun recommend(
        apparentTemperatureMin: Double,
        apparentTemperatureMax: Double
    ): Recommendation
}
