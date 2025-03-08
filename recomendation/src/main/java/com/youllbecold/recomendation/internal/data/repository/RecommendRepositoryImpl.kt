package com.youllbecold.recomendation.internal.data.repository

import android.util.Log
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.recomendation.api.RecommendRepository
import com.youllbecold.recomendation.internal.data.logs.LogCollector
import com.youllbecold.recomendation.internal.data.outfit.CustomOutfitSelector
import com.youllbecold.recomendation.internal.data.outfit.DefaultOutfitSelector
import com.youllbecold.recomendation.internal.data.rain.RainAdvisor
import com.youllbecold.recomendation.internal.data.uv.UvAdvisor
import com.youllbecold.recomendation.model.Certainty
import com.youllbecold.recomendation.model.Recommendation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementation of [RecommendRepository].
 */
internal class RecommendRepositoryImpl(
    private val logCollector: LogCollector
) : RecommendRepository {
    /**
     * Recommend clothes as:
     * - Gather most similar logs from database
     * - Adjust based on logged feeling
     * - Calculate certainty of recommendation
     * - If certainty is high enough, return, else return default recommendation
     * - Recommend UV and rain protection based on input data
     */
    override suspend fun recommend(
        hourlyApparentTemperatures: List<Double>,
        usesCelsius: Boolean,
        uvIndex: List<Double>,
        rainProbability: List<Int>
    ): Recommendation? = withContext(Dispatchers.Default) {
        if (hourlyApparentTemperatures.isEmpty()) {
            return@withContext null
        }

        // Calculate min and max temperatures and convert to Celsius as needed
        val minApparentC = hourlyApparentTemperatures.min().let {
            if (usesCelsius) it else fahrenheitToCelsius(it)
        }
        val maxApparentC = hourlyApparentTemperatures.max().let {
            if (usesCelsius) it else fahrenheitToCelsius(it)
        }

        val relevantLogs = logCollector.gatherRelevantLogs(minApparentC, maxApparentC)

        val best: Pair<List<Clothes>, Double>? =
            CustomOutfitSelector.createOutfitRecommendation(relevantLogs, minApparentC, maxApparentC)

        val (finalClothes, certaintyMeasure) = when {
            best == null || best.second < MEDIUM_CERTAINTY_MEASURE || best.first.isEmpty() -> {
                Log.d("RecommendRepository", "No similar logs found, using default outfit")
                DefaultOutfitSelector.createOutfitRecommendation(minTemp = minApparentC) to Certainty.Low
            }

            else -> {
                Log.d("RecommendRepository", "Found similar log")
                best.first to best.second.convertToCertainty()
            }
        }

        Recommendation(
            clothes = finalClothes,
            certainty = certaintyMeasure,
            uvLevel = UvAdvisor.uvRecommendation(uvIndex),
            rainLevel = RainAdvisor.rainRecommendation(rainProbability),
        )
    }

    private fun fahrenheitToCelsius(fahrenheit: Double): Double =
        (fahrenheit - 32) * 5 / 9

    private fun Double.convertToCertainty(): Certainty = when {
        this > HIGH_CERTAINTY_MEASURE -> Certainty.High
        this > MEDIUM_CERTAINTY_MEASURE -> Certainty.Medium
        else -> Certainty.Low
    }
}

private const val HIGH_CERTAINTY_MEASURE: Double = 0.8
private const val MEDIUM_CERTAINTY_MEASURE: Double = 0.5
