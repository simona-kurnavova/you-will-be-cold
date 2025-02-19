package com.youllbecold.trustme.ui.utils

import android.content.Context
import androidx.annotation.DrawableRes
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.IconType
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
