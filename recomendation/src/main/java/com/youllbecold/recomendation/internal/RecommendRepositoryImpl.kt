package com.youllbecold.recomendation.internal

import android.util.Log
import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.logdatabase.model.LogData
import com.youllbecold.recomendation.api.RecommendRepository
import com.youllbecold.recomendation.model.Certainty
import com.youllbecold.recomendation.model.RainRecommendation
import com.youllbecold.recomendation.model.Recommendation
import com.youllbecold.recomendation.model.UvRecommendation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class RecommendRepositoryImpl(
    private val logRepository: LogRepository
) : RecommendRepository {
    override suspend fun recommend(
        hourlyApparentTemperatures: List<Double>,
        usesCelsius: Boolean,
        uvIndex: List<Double>,
        rainProbability: List<Int>
    ): Recommendation = withContext(Dispatchers.Default) {
        // Calculate min and max temperatures and convert to Celsius as needed
        val minApparentC = hourlyApparentTemperatures.min().let {
            if (usesCelsius) it else fahrenheitToCelsius(it)
        }
        val maxApparentC = hourlyApparentTemperatures.max().let {
            if (usesCelsius) it else fahrenheitToCelsius(it)
        }

        // Get all weather logs - let's assume it is not overwhelming amount of data for now
        val allLogs = gatherLogs(minApparentC to maxApparentC)

        // For each log calculate similarity measure with current weather and adjust according to feelings
        val clothesWithSimilarity = allLogs.mapNotNull { log: LogData ->
            val weatherData = log.weatherData
            val weatherSimilarity = rangeSimilarityFactor(
                weatherData.apparentTemperatureMinC,
                weatherData.apparentTemperatureMaxC,
                minApparentC,
                maxApparentC,
            )

            if (weatherSimilarity <= MINIMAL_SIMILARITY_MEASURE) {
                return@mapNotNull null
            }

            val filteredClothes = log.clothes.replaceFullBodyClothes()
            val (clothes, certainty) = OutfitHelper.adjustClothes(filteredClothes, log.feelings)

            clothes to weatherSimilarity * certainty
        }.sortedByDescending { it.second }

        val similarLog = clothesWithSimilarity.firstOrNull()

        val (finalClothes, certaintyMeasure) = when {
            similarLog == null -> {
                Log.d("RecommendRepository", "No similar logs found, using default outfit")
                DefaultOutfitSelector.createRecommendation(minTemp = minApparentC) to Certainty.Low
            }

            else -> {
                Log.d("RecommendRepository", "Found similar log")
                similarLog.first to similarLog.second.convertToCertainty()
            }
        }

        Recommendation(
            clothes = finalClothes,
            certainty = certaintyMeasure,
            uvLevel = uvIndex.maxOfOrNull { uvRecommendation(it) }
                ?: UvRecommendation.LowProtection,
            rainLevel = rainProbability.maxOfOrNull { rainRecommendation(it) }
                ?: RainRecommendation.NoRain)
    }

    /**
     * Gather suitable logs from database. First try only specific range, then expand range if needed.
     */
    private suspend fun gatherLogs(
        apparentTempRange: Pair<Double, Double>
    ): List<LogData> =
        withContext(Dispatchers.IO) {
            var expandConstant = 0.0

            while (expandConstant <= MAX_RANGE_EXPAND) {
                val logs = logRepository.getLogsInRange(
                    apparentTempRange.first - expandConstant to apparentTempRange.second + expandConstant
                )

                if (logs.isNotEmpty()) {
                    return@withContext logs
                }
                expandConstant += 1.0
            }

            return@withContext emptyList()
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

    private fun fahrenheitToCelsius(fahrenheit: Double): Double =
        (fahrenheit - 32) * 5 / 9

    private fun rangeSimilarityFactor(tempFrom: Double, tempTo: Double, newTempFrom: Double, newTempTo: Double): Double {
        if (tempFrom > tempTo || newTempFrom > newTempTo) return 0.0 // Invalid range check

        val overlapStart = maxOf(tempFrom, newTempFrom)
        val overlapEnd = minOf(tempTo, newTempTo)
        val overlapLength = maxOf(0.0, overlapEnd - overlapStart)

        val unionLength = tempTo - tempFrom
        if (unionLength == 0.0) return 0.0 // Prevent division by zero

        return overlapLength / unionLength
    }

    private fun Double.convertToCertainty(): Certainty = when {
        this > HIGH_SIMILARITY_MEASURE -> Certainty.High
        this > MEDIUM_SIMILARITY_MEASURE -> Certainty.Medium
        else -> Certainty.Low
    }

    private fun List<Clothes>.replaceFullBodyClothes(): List<Clothes> =
        this.flatMap {
            when (it) {
                Clothes.SHORT_DRESS -> listOf(Clothes.SHORT_SLEEVE, Clothes.SHORT_SKIRT)
                Clothes.LONG_DRESS -> listOf(Clothes.SHORT_SLEEVE, Clothes.LONG_SKIRT)
                else -> listOf(it)
            }
        }
}

private const val MINIMAL_SIMILARITY_MEASURE: Double = 0.2
private const val MEDIUM_SIMILARITY_MEASURE: Double = 0.5
private const val HIGH_SIMILARITY_MEASURE: Double = 0.8

private const val MAX_RANGE_EXPAND = 5.0
