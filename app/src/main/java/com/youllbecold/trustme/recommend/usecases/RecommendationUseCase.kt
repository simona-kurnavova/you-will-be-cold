package com.youllbecold.trustme.recommend.usecases

import com.youllbecold.recomendation.api.RecommendRepository
import com.youllbecold.trustme.recommend.ui.model.RecommendationState
import com.youllbecold.trustme.recommend.ui.mappers.toRecommendationState
import com.youllbecold.weather.model.WeatherModel
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
    suspend fun recommend(hourlyWeatherModel: List<WeatherModel>): RecommendationState? =
        withContext(Dispatchers.Default) { // List operations
            if (hourlyWeatherModel.isEmpty()) {
                return@withContext null
            }

            val useCelsius = withContext(Dispatchers.IO) {
                hourlyWeatherModel.first().unitsCelsius
            }

            val rec = recommendRepository.recommend(
                hourlyWeatherModel.map { it.apparentTemperature },
                useCelsius,
                hourlyWeatherModel.map { it.uvIndex },
                hourlyWeatherModel.map { it.precipitationProbability }
            )

            return@withContext rec?.toRecommendationState()
        }
}
