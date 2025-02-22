package com.youllbecold.trustme.usecases.weather

import androidx.compose.runtime.Stable
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.recomendation.api.RecommendRepository
import com.youllbecold.recomendation.model.Certainty
import com.youllbecold.recomendation.model.RainRecommendation
import com.youllbecold.recomendation.model.UvRecommendation
import com.youllbecold.trustme.preferences.DataStorePreferences
import com.youllbecold.weather.model.Weather
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Singleton

@Singleton
class RecommendationUseCase(
    private val recommendRepository: RecommendRepository,
    private val dataStorePreferences: DataStorePreferences
) {
    private val dispatchers = Dispatchers.IO

    suspend fun recommend(hourlyWeather: List<Weather>): Recommendation =
        withContext(dispatchers) {
            val rec = recommendRepository.recommend(
                hourlyWeather.map { it.temperature },
                hourlyWeather.map { it.apparentTemperature },
                dataStorePreferences.useCelsiusUnits.first(),
                hourlyWeather.map { it.uvIndex },
                hourlyWeather.map { it.precipitationProbability }
            )

            Recommendation(
                uvLevel = when (rec.uvLevel) {
                    UvRecommendation.NoProtection -> UvLevelState.NONE
                    UvRecommendation.LowProtection -> UvLevelState.LOW
                    UvRecommendation.MediumProtection -> UvLevelState.MEDIUM
                    UvRecommendation.HighProtection -> UvLevelState.HIGH
                },

                rainLevel = when (rec.rainLevel) {
                    RainRecommendation.NoRain -> RainLevelState.NONE
                    RainRecommendation.LightRain -> RainLevelState.LOW
                    RainRecommendation.MediumRain -> RainLevelState.HIGH
                    RainRecommendation.HeavyRain -> RainLevelState.VERY_HIGH
                },
                clothes = rec.clothes.toPersistentList(),
                certainty = rec.certainty
            )
        }
}

@Stable
data class Recommendation(
    val uvLevel: UvLevelState,
    val rainLevel: RainLevelState,
    val clothes: PersistentList<Clothes>,
    val certainty: Certainty
)

enum class UvLevelState {
    NONE,
    LOW,
    MEDIUM,
    HIGH,
}

enum class RainLevelState {
    NONE,
    LOW,
    HIGH,
    VERY_HIGH,
}
