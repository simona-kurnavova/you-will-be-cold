package com.youllbecold.trustme.recommend.home.usecases

import com.youllbecold.trustme.common.ui.components.utils.millisToDateTime
import com.youllbecold.trustme.recommend.home.ui.model.Forecast
import com.youllbecold.trustme.recommend.usecases.RecommendationUseCase
import com.youllbecold.trustme.recommend.usecases.model.WeatherWithRecommendation
import com.youllbecold.weather.model.Weather
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import java.time.LocalDateTime

/**
 * Use case to create a [Forecast].
 */
class CreateForecastUseCase(
    private val recommendUseCase: RecommendationUseCase
) {

    /**
     * Create a [Forecast] - weather with recommendations from the hourly weather data and current weather.
     */
    suspend fun createForecast(
        currentWeather: Weather?,
        hourlyWeather: List<Weather>,
    ): Forecast? {
        if (currentWeather == null || hourlyWeather.isEmpty()) {
            return null
        }

        val now = LocalDateTime.now()

        val (allDayTodayWeather, tomorrowWeather) = hourlyWeather
            .partition { it.time.millisToDateTime.toLocalDate() == now.toLocalDate() }

        val todayWeather = allDayTodayWeather
            .filter { it.time.millisToDateTime.toLocalTime() >= now.toLocalTime() }

        return Forecast(
            current = WeatherWithRecommendation(
                persistentListOf(currentWeather),
                recommendUseCase.recommend(listOf(currentWeather))
            ),
            today = WeatherWithRecommendation(
                todayWeather.toPersistentList(),
                recommendUseCase.recommend(todayWeather)
            ),
            tomorrow = WeatherWithRecommendation(
                tomorrowWeather.toPersistentList(),
                recommendUseCase.recommend(tomorrowWeather)
            )
        )
    }
}
