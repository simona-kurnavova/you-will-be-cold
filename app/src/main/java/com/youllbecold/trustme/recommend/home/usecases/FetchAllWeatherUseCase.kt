package com.youllbecold.trustme.recommend.home.usecases

import com.youllbecold.trustme.common.domain.weather.CurrentWeatherProvider
import com.youllbecold.trustme.common.domain.weather.HourlyWeatherProvider
import com.youllbecold.trustme.common.ui.components.utils.millisToDateTime
import com.youllbecold.trustme.common.ui.model.status.LoadingStatus
import com.youllbecold.trustme.recommend.home.ui.mappers.toHourlyTemperatures
import com.youllbecold.trustme.recommend.home.ui.model.Forecast
import com.youllbecold.trustme.recommend.home.ui.model.HourlyTemperature
import com.youllbecold.trustme.recommend.ui.mappers.toWeatherCondPersistList
import com.youllbecold.trustme.recommend.ui.mappers.toWeatherConditions
import com.youllbecold.trustme.recommend.usecases.RecommendationUseCase
import com.youllbecold.trustme.recommend.ui.model.WeatherWithRecommendation
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDateTime

/**
 * Use case to create a [Forecast].
 */
class FetchAllWeatherUseCase(
    private val hourlyWeatherProvider: HourlyWeatherProvider,
    private val currentWeatherProvider: CurrentWeatherProvider,
    private val recommendUseCase: RecommendationUseCase
) {

    /**
     * Create a [Forecast] - weather with recommendations from the hourly weather data and current weather.
     */
    suspend fun fetchWeather(useCelsius: Boolean): AllWeatherWithStatus {
        val current = currentWeatherProvider.fetchCurrentWeather(useCelsius)
        if (current.status != LoadingStatus.Success) {
            return AllWeatherWithStatus(status = current.status)
        }

        val hourly = hourlyWeatherProvider.fetchHourlyWeather(useCelsius, DAYS_FOR_HOURLY_WEATHER)
        if (hourly.status != LoadingStatus.Success) {
            return AllWeatherWithStatus(status = hourly.status)
        }

        val now = LocalDateTime.now()

        val (allDayTodayWeather, tomorrowWeather) = hourly.weatherModel
            .partition { it.time.millisToDateTime.toLocalDate() == now.toLocalDate() }

        val todayWeather = allDayTodayWeather
            .filter { it.time.millisToDateTime.toLocalTime() >= now.toLocalTime() }

        val currentWeather = current.weatherModel
            ?: return AllWeatherWithStatus(status = LoadingStatus.GenericError)

        val forecast = Forecast(
            current = WeatherWithRecommendation(
                persistentListOf(currentWeather.toWeatherConditions()),
                recommendUseCase.recommend(listOf(currentWeather))
            ),
            today = WeatherWithRecommendation(
                todayWeather.toWeatherCondPersistList(),
                recommendUseCase.recommend(todayWeather)
            ),
            tomorrow = WeatherWithRecommendation(
                tomorrowWeather.toWeatherCondPersistList(),
                recommendUseCase.recommend(tomorrowWeather)
            )
        )

        return AllWeatherWithStatus(
            status = LoadingStatus.Success,
            forecast = forecast,
            hourlyTemperatures = forecast.toHourlyTemperatures()
        )
    }
}

private const val DAYS_FOR_HOURLY_WEATHER = 2

data class AllWeatherWithStatus(
    val status: LoadingStatus,
    val forecast: Forecast? = null,
    val hourlyTemperatures: PersistentList<HourlyTemperature> = persistentListOf(),
)
