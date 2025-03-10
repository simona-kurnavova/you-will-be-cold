package com.youllbecold.trustme.recommend.usecases

import android.app.Application
import com.youllbecold.recomendation.api.RecommendRepository
import com.youllbecold.trustme.recommend.usecases.model.RecommendationState
import com.youllbecold.trustme.recommend.usecases.model.mappers.toRecommendationState
import com.youllbecold.weather.model.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Use case for recommendation.
 */
class RecommendationUseCase(
    private val app: Application,
    private val recommendRepository: RecommendRepository,
) {
    /**
     * Recommends clothes based on the weather.
     */
    suspend fun recommend(hourlyWeather: List<Weather>): RecommendationState? =
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

            return@withContext rec?.toRecommendationState(app)
        }
}