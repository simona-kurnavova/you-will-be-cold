package com.youllbecold.trustme.recommend.home.ui.model

import androidx.compose.runtime.Stable
import com.youllbecold.trustme.recommend.ui.model.WeatherWithRecommendation

@Stable
data class Forecast(
    val current: WeatherWithRecommendation,
    val today: WeatherWithRecommendation,
    val tomorrow: WeatherWithRecommendation
)
