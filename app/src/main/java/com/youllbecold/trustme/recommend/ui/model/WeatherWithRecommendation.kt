package com.youllbecold.trustme.recommend.ui.model

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.PersistentList

@Stable
data class WeatherWithRecommendation(
    val weather: PersistentList<WeatherConditions>,
    val recommendationState: RecommendationState?
)

/**
 * Get the weather temperature units used. We assume whole list will have the same units.
 */
val WeatherWithRecommendation.unitsCelsius: Boolean?
    get() = this.weather.firstOrNull()?.unitsCelsius
