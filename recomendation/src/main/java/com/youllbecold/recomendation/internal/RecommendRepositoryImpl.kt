package com.youllbecold.recomendation.internal

import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.recomendation.api.RecommendRepository
import com.youllbecold.recomendation.model.Certainty
import com.youllbecold.recomendation.model.RainRecommendation
import com.youllbecold.recomendation.model.Recommendation
import com.youllbecold.recomendation.model.UvRecommendation

internal class RecommendRepositoryImpl(
    private val logRepository: LogRepository
) : RecommendRepository {

    override suspend fun recommend(
        hourlyTemperatures: List<Double>,
        hourlyApparentTemperatures: List<Double>,
        uvIndex: List<Double>,
        rainProbability: List<Int>
    ): Recommendation {
        // TODO: Find previous logs similar to the current weather.

        // TODO: Calculate outfit weight

        // TODO: Recommend based on:
        // 1. Feeling
        // 2. Difference of weather

        return Recommendation(
            clothes = listOf(Clothes.JEANS, Clothes.LONG_SLEEVE, Clothes.TENNIS_SHOES),
            certainty = Certainty.Low,
            basedOn = emptyList(),
            uvLevel = uvIndex.maxOfOrNull { uvRecommendation(it) } ?: UvRecommendation.LowProtection,
            rainLevel = rainProbability.maxOfOrNull { rainRecommendation(it) } ?: RainRecommendation.NoRain
        )
    }

    private fun uvRecommendation(uvIndex: Double): UvRecommendation {
        return when {
            uvIndex <= 0 -> UvRecommendation.NoProtection
            uvIndex < 3 -> UvRecommendation.LowProtection
            uvIndex < 6 -> UvRecommendation.MediumProtection
            else -> UvRecommendation.HighProtection
        }
    }

    private fun rainRecommendation(rainProbability: Int): RainRecommendation {
        return when {
            rainProbability < 1 -> RainRecommendation.NoRain
            rainProbability < 5 -> RainRecommendation.LightRain
            rainProbability < 8 -> RainRecommendation.MediumRain
            else -> RainRecommendation.HeavyRain
        }
    }
}