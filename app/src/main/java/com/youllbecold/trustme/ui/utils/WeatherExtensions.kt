package com.youllbecold.trustme.ui.utils

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.IconType
import com.youllbecold.trustme.ui.viewmodels.WeatherWithRecommendation
import com.youllbecold.trustme.usecases.weather.RainLevelState
import com.youllbecold.trustme.usecases.weather.UvLevelState
import com.youllbecold.weather.model.Weather
import com.youllbecold.weather.model.WeatherEvaluation

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
        WeatherEvaluation.UNKNOWN -> IconType.Cloud
    }

/**
 * Get the thermometer image based on the Weather temperature.
 */
@get:DrawableRes
val Weather.thermometer: Int
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

/**
 * Get the temperature string based on the units.
 */
fun Context.getTemperatureString(temperature: Double, unitsCelsius: Boolean): String =
    if (unitsCelsius) {
        getString(R.string.temperature_celsius, temperature)
    } else {
        getString(R.string.temperature_fahrenheit, temperature)
    }

@Composable
fun UvLevelState.getTitle(): String = when (this) {
    UvLevelState.NONE -> stringResource(R.string.uv_recom_none)
    UvLevelState.LOW -> stringResource(R.string.uv_recom_low)
    UvLevelState.MEDIUM -> stringResource(R.string.uv_recom_medium)
    UvLevelState.HIGH -> stringResource(R.string.uv_recom_high)
}

@Composable
fun RainLevelState.getTitle(): String = when (this) {
    RainLevelState.NONE -> stringResource(R.string.rain_recom_none)
    RainLevelState.LOW -> stringResource(R.string.rain_recom_low)
    RainLevelState.HIGH -> stringResource(R.string.rain_recom_medium)
    RainLevelState.VERY_HIGH -> stringResource(R.string.rain_recom_high)
}

@Composable
fun WeatherWithRecommendation.rangeDescription(): String {
    val context = LocalContext.current
    val usesCelsius = weather.first().unitsCelsius

    val min = context.getTemperatureString(weather.minOf { it.temperature }, usesCelsius)
    val max = context.getTemperatureString(weather.maxOf { it.temperature }, usesCelsius)

    if (weather.size == 1) {
        return stringResource(R.string.recom_description_single, min)
    }

    return stringResource(R.string.recom_description_range, min, max)
}

@Composable
fun WeatherWithRecommendation.feelLikeDescription(): String {
    val context = LocalContext.current
    val usesCelsius = weather.first().unitsCelsius

    val min = context.getTemperatureString(weather.minOf { it.apparentTemperature }, usesCelsius)
    val max = context.getTemperatureString(weather.maxOf { it.apparentTemperature }, usesCelsius)

    if (weather.size == 1) {
        return stringResource(R.string.recom_apparent_description_single, min)
    }

    return stringResource(R.string.recom_apparent_description, min, max)
}