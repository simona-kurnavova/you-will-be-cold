package com.youllbecold.weather.model

import androidx.compose.runtime.Stable

/**
 * Weather evaluation.
 */
@Stable
enum class WeatherEvaluation {
    SUNNY,
    CLOUDY,
    FOGGY,
    RAINY,
    SNOWY,
    STORM,
    UNKNOWN
}
