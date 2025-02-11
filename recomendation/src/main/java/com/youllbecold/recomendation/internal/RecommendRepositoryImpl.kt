package com.youllbecold.recomendation.internal

import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.recomendation.api.RecommendRepository
import com.youllbecold.recomendation.model.Certainty
import com.youllbecold.recomendation.model.Recommendation

internal class RecommendRepositoryImpl(
    private val logRepository: LogRepository
) : RecommendRepository {

    override suspend fun recommend(
        apparentTemperatureMin: Double,
        apparentTemperatureMax: Double
    ): Recommendation {
        // TODO: Find previous logs similar to the current weather.

        // TODO: Calculate outfit weight

        // TODO: Recommend based on:
        // 1. Feeling
        // 2. Difference of weather

        return Recommendation(
            clothes = listOf(Clothes.JEANS, Clothes.LONG_SLEEVE, Clothes.TENNIS_SHOES),
            certainty = Certainty.Low,
            basedOn = emptyList()
        )
    }
}