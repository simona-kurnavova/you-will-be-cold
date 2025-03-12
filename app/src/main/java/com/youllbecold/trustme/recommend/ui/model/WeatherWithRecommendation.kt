package com.youllbecold.trustme.recommend.ui.model

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.PersistentList

@Stable
data class WeatherWithRecommendation(
    val weather: PersistentList<WeatherConditions>,
    val recommendationState: RecommendationState?
)
