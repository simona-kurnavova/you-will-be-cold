package com.youllbecold.trustme.common.domain.usecases.recommendation

import com.youllbecold.recomendation.api.RecommendRepository
import com.youllbecold.trustme.common.ui.model.recommendation.Recommendation
import com.youllbecold.weather.model.Weather
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Use case for recommendation.
 */
class RecommendationUseCase(
    private val recommendRepository: RecommendRepository,
) {
    /**
     * Recommends clothes based on the weather.
     */
    suspend fun recommend(hourlyWeather: List<Weather>): Recommendation? =
        withContext(Dispatchers.Default) {
            if (hourlyWeather.isEmpty()) {
                return@withContext null
            }

            val useCelsius = withContext(Dispatchers.IO) {
                hourlyWeather.first().unitsCelsius
            }

            val rec = recommendRepository.recommend(
                hourlyWeather.map { it.apparentTemperature },
                useCelsius,
                hourlyWeather.map { it.uvIndex },
                hourlyWeather.map { it.precipitationProbability }
            )

            return@withContext rec?.let {
                Recommendation(
                    uvLevel = rec.uvLevel,
                    rainLevel = rec.rainLevel,
                    clothes = rec.clothes.toPersistentList(),
                    certainty = rec.certainty
                )
            }
        }
}
