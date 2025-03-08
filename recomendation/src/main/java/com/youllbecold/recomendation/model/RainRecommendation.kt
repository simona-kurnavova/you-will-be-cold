package com.youllbecold.recomendation.model

import androidx.compose.runtime.Stable

/**
 * Rain level recommendation.
 */
@Stable
enum class RainRecommendation {
    NoRain,
    LightRain,
    MediumRain,
    HeavyRain,
}
