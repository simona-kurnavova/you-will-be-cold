package com.youllbecold.trustme.recommend.ui.model

import androidx.compose.runtime.Stable
import com.youllbecold.trustme.common.ui.components.themed.IconType

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
