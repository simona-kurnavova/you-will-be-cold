package com.youllbecold.trustme.recommend.domain

import com.youllbecold.recomendation.api.RecommendRepository
import com.youllbecold.trustme.common.ui.model.status.Error.GenericError
import com.youllbecold.trustme.common.ui.model.status.Idle
import com.youllbecold.trustme.common.ui.model.status.Status
import com.youllbecold.trustme.common.ui.model.status.Success
import com.youllbecold.trustme.recommend.ui.mappers.toRecommendationState
import com.youllbecold.trustme.recommend.ui.model.RecommendationState
import com.youllbecold.weather.model.WeatherModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Simple provider for recommendation.
 */
class RecommendationProvider(
    private val recommendRepository: RecommendRepository,
) {
    /**
     * Recommends clothes based on the weather.
     */
    suspend fun recommend(hourlyWeatherModel: List<WeatherModel>): RecommendationWithStatus =
        withContext(Dispatchers.Default) { // List operations
            if (hourlyWeatherModel.isEmpty()) {
                return@withContext RecommendationWithStatus(GenericError)
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


            val recommendation = rec?.toRecommendationState()

            return@withContext RecommendationWithStatus(
                recommendation = recommendation,
                status = if (recommendation != null) {
                    Success
                } else {
                    GenericError
                }
            )
        }
}

data class RecommendationWithStatus(
    val status: Status = Idle,
    val recommendation: RecommendationState? = null,
)
