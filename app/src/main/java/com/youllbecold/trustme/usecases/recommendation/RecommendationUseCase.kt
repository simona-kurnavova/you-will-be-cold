package com.youllbecold.trustme.usecases.recommendation

import androidx.compose.runtime.Stable
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.recomendation.api.RecommendRepository
import com.youllbecold.recomendation.model.Certainty
import com.youllbecold.recomendation.model.RainRecommendation
import com.youllbecold.recomendation.model.UvRecommendation
import com.youllbecold.weather.model.Weather
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Singleton

/**
 * Use case for recommendation.
 */
@Singleton
class RecommendationUseCase(
    private val recommendRepository: RecommendRepository,
) {
    private val dispatchers = Dispatchers.IO

    /**
     * Recommends clothes based on the weather.
     */
    suspend fun recommend(hourlyWeather: List<Weather>): Recommendation? =
        withContext(dispatchers) {
            val useCelsius = hourlyWeather.firstOrNull()?.unitsCelsius
                ?: return@withContext null

            val rec = recommendRepository.recommend(
                hourlyWeather.map { it.apparentTemperature },
                useCelsius,
                hourlyWeather.map { it.uvIndex },
                hourlyWeather.map { it.precipitationProbability }
            )

            if (rec == null) {
                return@withContext null
            }

            Recommendation(
                uvLevel = rec.uvLevel,
                rainLevel = rec.rainLevel,
                clothes = rec.clothes.toPersistentList(),
                certainty = rec.certainty
            )
        }
}

/**
 * Data class for recommendation.
 */
@Stable
data class Recommendation(
    val uvLevel: UvRecommendation,
    val rainLevel: RainRecommendation,
    val clothes: PersistentList<Clothes>,
    val certainty: Certainty
)
