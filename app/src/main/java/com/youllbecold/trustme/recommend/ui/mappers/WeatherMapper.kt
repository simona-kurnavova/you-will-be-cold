package com.youllbecold.trustme.recommend.ui.mappers

import androidx.annotation.DrawableRes
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.components.themed.IconType
import com.youllbecold.trustme.recommend.ui.model.WeatherConditions
import com.youllbecold.weather.model.WeatherModel
import com.youllbecold.weather.model.WeatherEvaluation
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Map [WeatherModel] to [WeatherConditions] to be used in UI.
 */
fun WeatherModel.toWeatherConditions(): WeatherConditions =
    WeatherConditions(
        time = time,
        unitsCelsius = unitsCelsius,
        temperature = temperature,
        apparentTemperature = apparentTemperature,
        icon = weatherEvaluation.icon,
        relativeHumidity = relativeHumidity,
        windSpeed = windSpeed,
        precipitationProbability = precipitationProbability,
        uvIndex = uvIndex
    )

/**
 * Map [List] of [WeatherModel] to [PersistentList] of [WeatherConditions] to be used in UI.
 */
suspend fun List<WeatherModel>.toWeatherCondPersistList(): PersistentList<WeatherConditions> =
    withContext(Dispatchers.Default) {
        map {
            it.toWeatherConditions()
        }.toPersistentList()
    }

/**
 * Get icon for [WeatherEvaluation].
 */
val WeatherEvaluation.icon: IconType
    get() = when (this) {
        WeatherEvaluation.SUNNY -> IconType.Sun
        WeatherEvaluation.CLOUDY -> IconType.Cloud
        WeatherEvaluation.RAINY -> IconType.Rain
        WeatherEvaluation.SNOWY -> IconType.Snowflake
        WeatherEvaluation.STORM -> IconType.Lightning
        WeatherEvaluation.FOGGY -> IconType.Fog
        WeatherEvaluation.UNKNOWN -> IconType.Cloud // Why? Because I said so. It is cloudy now.
    }

/**
 * Get the thermometer image based on the Weather temperature.
 */
@get:DrawableRes
val WeatherConditions.thermometer: Int
    get() {
        val celsius = if (unitsCelsius) temperature else fahrenheitToCelsius(temperature)
        return when {
            celsius < 0 -> R.drawable.thermometer_0
            celsius < 10 -> R.drawable.thermometer_1
            celsius < 20 -> R.drawable.thermometer_2
            else -> R.drawable.thermometer_3
        }
    }

private fun fahrenheitToCelsius(fahrenheit: Double): Double =
    (fahrenheit - 32) * 5 / 9
