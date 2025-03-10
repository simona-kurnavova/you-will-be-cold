package com.youllbecold.trustme.recommend.usecases.model

import androidx.compose.runtime.Stable
import com.youllbecold.weather.model.Weather
import kotlinx.collections.immutable.PersistentList

@Stable
data class WeatherWithRecommendation(
    val weather: PersistentList<Weather>,
    val recommendationState: RecommendationState?
)