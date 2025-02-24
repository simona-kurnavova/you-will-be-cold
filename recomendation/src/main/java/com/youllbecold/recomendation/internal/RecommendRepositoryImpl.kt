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
import kotlin.collections.distinct

internal class RecommendRepositoryImpl(
    private val logRepository: LogRepository
) : RecommendRepository {
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

        // Get all relevant weather logs
        val allLogs = gatherLogs(minApparentC to maxApparentC)

        // Calculate best similar candidate with certainty measure
        val best = calculateBestCandidate(allLogs, minApparentC, maxApparentC)

        val (finalClothes, certaintyMeasure) = when {
            best == null || best.second < MEDIUM_CERTAINTY_MEASURE || best.first.isEmpty() -> {
                Log.d("RecommendRepository", "No similar logs found, using default outfit")
                DefaultOutfitSelector.createRecommendation(minTemp = minApparentC) to Certainty.Low
            }

            else -> {
                Log.d("RecommendRepository", "Found similar log")
                best.first to best.second.convertToCertainty()
            }
        }

        Recommendation(
            clothes = finalClothes,
            certainty = certaintyMeasure,
            uvLevel = uvIndex.maxOfOrNull { uvRecommendation(it) } ?: UvRecommendation.LowProtection,
            rainLevel = rainProbability.maxOfOrNull { rainRecommendation(it) } ?: RainRecommendation.NoRain
        )
    }

    /**
     * Gather suitable logs from database. First try only specific range, then expand range if needed.
     */
    private suspend fun gatherLogs(apparentTempRange: Pair<Double, Double>): List<LogData> =
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

    private suspend fun calculateBestCandidate(
        logs: List<LogData>,
        minTemp: Double,
        maxTemp: Double
    ): Pair<List<Clothes>, Double>? = withContext(Dispatchers.Default) {
        if (logs.isEmpty()) {
            return@withContext null
        }

        var bestClothes: MutableMap<BodyPart, ClothesWithCertainty> = mutableMapOf()

        logs.map { log ->
            val weatherData = log.weatherData

            log to rangeSimilarityFactor(
                weatherData.apparentTemperatureMinC,
                weatherData.apparentTemperatureMaxC,
                minTemp,
                maxTemp,
            )
        }
            .filter { it.second >= MEDIUM_CERTAINTY_MEASURE }
            .sortedByDescending { it.second } // Sort by similarity
            .take(MAX_LOGS_PROCESSING) // Limit to certain number to optimise performance and prioritise latest logs
            .forEach { (log, similarity) ->
                val clothesCertainty = OutfitHelper.calculateCertainty(log.feelings)

                clothesCertainty.forEach { bodyPart ->
                    val bodyPartMatch = similarity * bodyPart.value

                    if (bodyPartMatch > (bestClothes[bodyPart.key]?.certainty ?: 0.0)) {
                        bestClothes[bodyPart.key] = ClothesWithCertainty(
                            clothes = OutfitHelper.adjustPerFeeling(
                                clothes = log.clothes,
                                feeling = bodyPart.key.getFeeling(log.feelings),
                                bodyPart = bodyPart.key
                            ),
                            certainty = bodyPartMatch
                        )
                    }
                }
            }

        val finalClothes = bestClothes.flatMap { it.value.clothes }.distinct()
        val certainty = bestClothes.values.map { it.certainty }.average()

        return@withContext Pair(finalClothes, certainty)
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
        this > HIGH_CERTAINTY_MEASURE -> Certainty.High
        this > MEDIUM_CERTAINTY_MEASURE -> Certainty.Medium
        else -> Certainty.Low
    }
}

private const val HIGH_CERTAINTY_MEASURE: Double = 0.8
private const val MEDIUM_CERTAINTY_MEASURE: Double = 0.5

private const val MAX_RANGE_EXPAND = 5.0
private const val MAX_LOGS_PROCESSING = 50

data class ClothesWithCertainty(
    val clothes: List<Clothes> = emptyList(),
    val certainty: Double = 0.0
)
