package com.youllbecold.trustme.common.ui.model.weatherwithrecommend

import androidx.compose.runtime.Stable
import com.youllbecold.trustme.common.ui.model.recommendation.Recommendation
import com.youllbecold.weather.model.Weather
import kotlinx.collections.immutable.PersistentList

@Stable
data class WeatherWithRecommendation(
    val weather: PersistentList<Weather>,
    val recommendation: Recommendation?
)
