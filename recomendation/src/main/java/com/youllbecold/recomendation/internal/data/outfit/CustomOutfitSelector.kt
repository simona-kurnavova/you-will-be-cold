package com.youllbecold.recomendation.internal.data.outfit

import com.youllbecold.logdatabase.model.ClothesModel
import com.youllbecold.logdatabase.model.LogData
import com.youllbecold.recomendation.internal.data.utils.LogCertaintyUtils
import com.youllbecold.recomendation.internal.data.utils.TemperatureSimilarityUtils
import com.youllbecold.recomendation.internal.data.utils.getFeeling
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Custom outfit selector that uses user logs to recommend clothes.
 */
internal object CustomOutfitSelector {
    /**
     * Create outfit recommendation based on user logs.
     *
     * @param logs List of user logs - it is up to caller how accurate they are.
     * @param minApparentTemp Minimum apparent temperature in Celsius.
     * @param maxApparentTemp Maximum apparent temperature in Celsius.
     */
    suspend fun createOutfitRecommendation(
        logs: List<LogData>,
        minApparentTemp: Double,
        maxApparentTemp: Double,
    ): Pair<List<ClothesModel>, Double>? = withContext(Dispatchers.Default) {
        if (logs.isEmpty()) {
            return@withContext null
        }

        var bestClothes: MutableMap<BodyPart, ClothesWithCertainty> = mutableMapOf()

        logs
            .filterAndMapToSimilarity(minApparentTemp, maxApparentTemp)
            .forEach { (log, similarity) ->
                val clothesCertainty = LogCertaintyUtils.calculateCertaintyMap(log.feelings)

                clothesCertainty.forEach { bodyPart ->
                    val bodyPartMatch = similarity * bodyPart.value

                    if (bodyPartMatch > (bestClothes[bodyPart.key]?.certainty ?: 0.0)) {
                        bestClothes[bodyPart.key] = ClothesWithCertainty(
                            clothes = OutfitProcessor.adjustPerFeeling(
                                clothes = log.clothes,
                                feeling = bodyPart.key.getFeeling(log.feelings),
                                bodyPart = bodyPart.key
                            ),
                            certainty = bodyPartMatch
                        )
                    }
                }
            }

        return@withContext bestClothes.mapToResult()
    }

    private fun List<LogData>.filterAndMapToSimilarity(
        minApparentTemp: Double,
        maxApparentTemp: Double
    ) =
        map { log ->
            val weatherData = log.weatherData

            log to TemperatureSimilarityUtils.calculateRangeSimilarity(
                weatherData.apparentTemperatureMinC,
                weatherData.apparentTemperatureMaxC,
                minApparentTemp,
                maxApparentTemp,
            )
        }
            .filter { it.second >= MINIMUM_CERTAINTY_MEASURE }
            .sortedByDescending { it.second } // Sort by similarity
            .take(MAX_LOGS_PROCESSING) // Limit to certain number to optimise performance and prioritise latest logs

    private fun Map<BodyPart, ClothesWithCertainty>.mapToResult(): Pair<List<ClothesModel>, Double> {
        val finalClothes = this
            .flatMap { it.value.clothes }
            .distinct()

        val certainty = this.values
            .map { it.certainty }
            .average()

        return Pair(finalClothes, certainty)
    }

    private data class ClothesWithCertainty(
        val clothes: List<ClothesModel> = emptyList(),
        val certainty: Double = 0.0
    )
}

private const val MAX_LOGS_PROCESSING = 50
private const val MINIMUM_CERTAINTY_MEASURE: Double = 0.5
