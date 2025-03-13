package com.youllbecold.trustme.recommend.ui.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Stable
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.components.themed.IconType
import com.youllbecold.trustme.common.ui.utils.TemperatureUiUtils

@Stable
data class WeatherConditions(
    val time: Long,
    val unitsCelsius: Boolean,
    val temperature: Double,
    val apparentTemperature: Double,
    val icon: IconType,
    val relativeHumidity: Int,
    val windSpeed: Double,
    val precipitationProbability: Int,
    val uvIndex: Double,
)

/**
 * Get the thermometer image based on the Weather temperature.
 */
@get:DrawableRes
val WeatherConditions.thermometer: Int
    get() {
        val celsius = if (unitsCelsius) temperature else TemperatureUiUtils.fahrenheitToCelsius(temperature)
        return when {
            celsius < 0 -> R.drawable.thermometer_0
            celsius < 10 -> R.drawable.thermometer_1
            celsius < 20 -> R.drawable.thermometer_2
            else -> R.drawable.thermometer_3
        }
    }
