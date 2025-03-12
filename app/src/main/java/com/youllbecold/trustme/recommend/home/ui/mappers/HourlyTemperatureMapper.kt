package com.youllbecold.trustme.recommend.home.ui.mappers

import com.youllbecold.trustme.common.ui.components.utils.formatTime
import com.youllbecold.trustme.common.ui.components.utils.millisToDateTime
import com.youllbecold.trustme.recommend.home.ui.model.Forecast
import com.youllbecold.trustme.recommend.home.ui.model.HourlyTemperature
import com.youllbecold.trustme.recommend.ui.model.WeatherConditions
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlin.math.roundToInt

/**
 * The next 24 hours of weather temperature forecast.
 */
fun Forecast.toHourlyTemperatures(): PersistentList<HourlyTemperature> =
    (today.weather + tomorrow.weather)
        .take(HOURS_IN_HOURLY_WEATHER)
        .toHourlyTemperature()
        .toPersistentList()

private fun List<WeatherConditions>?.toHourlyTemperature(): PersistentList<HourlyTemperature> =
    this?.map { weather ->
        HourlyTemperature(
            formattedTime = weather.time
                .millisToDateTime
                .toLocalTime()
                .formatTime(),
            temperature = weather.temperature.roundToInt(),
            weatherIcon = weather.icon
        )
    }?.toPersistentList() ?: persistentListOf()

private const val HOURS_IN_HOURLY_WEATHER = 24
