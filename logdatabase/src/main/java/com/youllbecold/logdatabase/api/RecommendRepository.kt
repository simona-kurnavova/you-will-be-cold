package com.youllbecold.logdatabase.api


/**
 * A repository for recommendations.
 */
interface RecommendRepository {
    suspend fun recommend(
        apparentTemperatureMin: Double,
        apparentTemperatureMax: Double
    )
}