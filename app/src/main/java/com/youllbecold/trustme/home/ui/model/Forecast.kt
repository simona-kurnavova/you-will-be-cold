package com.youllbecold.trustme.home.ui.model

import androidx.compose.runtime.Stable
import com.youllbecold.trustme.common.ui.components.utils.millisToDateTime
import com.youllbecold.trustme.common.ui.model.weather.mappers.icon
import com.youllbecold.trustme.common.ui.model.weatherwithrecommend.WeatherWithRecommendation
import com.youllbecold.weather.model.Weather
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.roundToInt

@Stable
data class Forecast(
    val current: WeatherWithRecommendation,
    val today: WeatherWithRecommendation,
    val tomorrow: WeatherWithRecommendation
) {
    /**
     * The next 24 hours of weather forecast.
     */
    fun next24Hours(): PersistentList<HourlyTemperature> =
        (today.weather + tomorrow.weather)
            .take(HOURS_IN_HOURLY_WEATHER)
            .toHourlyTemperature()
            .toPersistentList()

    private fun List<Weather>?.toHourlyTemperature(): PersistentList<HourlyTemperature> =
        this?.map { weather ->
            HourlyTemperature(
                formattedTime = weather.time.formatTime(),
                temperature = weather.temperature.roundToInt(),
                weatherIcon = weather.weatherEvaluation.icon
            )
        }?.toPersistentList() ?: persistentListOf()

    private fun Long.formatTime(): String {
        val formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
        return this.millisToDateTime.format(formatter)
    }
}

private const val HOURS_IN_HOURLY_WEATHER = 24
