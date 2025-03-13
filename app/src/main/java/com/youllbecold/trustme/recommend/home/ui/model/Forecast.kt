package com.youllbecold.trustme.recommend.home.ui.model

import androidx.compose.runtime.Stable
import com.youllbecold.trustme.recommend.ui.model.WeatherWithRecommendation
import com.youllbecold.trustme.recommend.ui.model.unitsCelsius

@Stable
data class Forecast(
    val current: WeatherWithRecommendation,
    val today: WeatherWithRecommendation,
    val tomorrow: WeatherWithRecommendation
)

/**
 * Get the weather temperature units used. We assume whole list will have the same units.
 */
val Forecast.unitsCelsius: Boolean?
    get() = this.current.unitsCelsius
