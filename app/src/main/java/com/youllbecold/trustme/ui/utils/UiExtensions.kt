package com.youllbecold.trustme.ui.utils

import android.content.Context
import androidx.annotation.DrawableRes
import com.youllbecold.trustme.R
import com.youllbecold.weather.model.Weather
import com.youllbecold.weather.model.WeatherEvaluation

@DrawableRes
fun WeatherEvaluation.icon(): Int = when (this) {
    WeatherEvaluation.SUNNY -> R.drawable.ic_sun
    WeatherEvaluation.CLOUDY -> R.drawable.ic_cloud
    WeatherEvaluation.RAINY -> R.drawable.ic_rain
    WeatherEvaluation.SNOWY -> R.drawable.ic_snow
    WeatherEvaluation.STORM -> R.drawable.ic_lightning
    WeatherEvaluation.FOGGY -> R.drawable.ic_cloud
    WeatherEvaluation.UNKNOWN -> R.drawable.ic_cloud
}

@DrawableRes
fun Weather.thermometerImage(): Int =
    if (unitsCelsius) {
        when {
            temperature < 0 -> R.drawable.thermometer_0
            temperature < 10 -> R.drawable.thermometer_1
            temperature < 20 -> R.drawable.thermometer_2
            else -> R.drawable.thermometer_3
        }
    } else {
        when {
            temperature < 32 -> R.drawable.thermometer_0
            temperature < 50 -> R.drawable.thermometer_1
            temperature < 68 -> R.drawable.thermometer_2
            else -> R.drawable.thermometer_3
        }
    }

fun Context.getTemperatureString(temperature: Double, unitsCelsius: Boolean): String =
    if (unitsCelsius) {
        getString(R.string.temperature_celsius, temperature)
    } else {
        getString(R.string.temperature_fahrenheit, temperature)
    }
