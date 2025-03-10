package com.youllbecold.trustme.home.ui.model

import androidx.compose.runtime.Stable
import com.youllbecold.trustme.common.ui.model.weatherwithrecommend.WeatherWithRecommendation

@Stable
data class Forecast(
    val current: WeatherWithRecommendation,
    val today: WeatherWithRecommendation,
    val tomorrow: WeatherWithRecommendation
)
