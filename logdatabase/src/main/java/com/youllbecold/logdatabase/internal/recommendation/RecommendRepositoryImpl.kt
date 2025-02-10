package com.youllbecold.logdatabase.internal.recommendation

import com.youllbecold.logdatabase.api.RecommendRepository
import com.youllbecold.logdatabase.internal.log.LogDao

internal class RecommendRepositoryImpl(
    private val logDao: LogDao
) : RecommendRepository {

    override suspend fun recommend(
        apparentTemperatureMin: Double,
        apparentTemperatureMax: Double
    ) {
        // TODO: Find previous logs similar to the current weather.

        // TODO: Calculate outfit weight

        // TODO: Recommend based on:
        // 1. Feeling
        // 2. Difference of weather
    }
}