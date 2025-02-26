package com.youllbecold.trustme.ui.viewmodels.state

import androidx.compose.runtime.Stable
import com.youllbecold.trustme.usecases.recommendation.Recommendation
import com.youllbecold.weather.model.Weather
import kotlinx.collections.immutable.PersistentList

@Stable
data class WeatherWithRecommendation(
    val weather: PersistentList<Weather>,
    val recommendation: Recommendation?
)
