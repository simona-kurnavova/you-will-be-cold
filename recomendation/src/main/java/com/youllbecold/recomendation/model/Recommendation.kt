package com.youllbecold.recomendation.model

import androidx.compose.runtime.Stable
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.logdatabase.model.LogData

/**
 * Data class for recommendation.
 */
@Stable
data class Recommendation(
    val clothes: List<Clothes> = emptyList(),
    val certainty: Certainty = Certainty.Low,
    val uvLevel: UvRecommendation,
    val rainLevel: RainRecommendation
)

/**
 * Certainty level of the recommendation.
 */
@Stable
enum class Certainty {
    /**
     * Default value was used, we are just guessing here.
     */
    Low,

    /**
     * Derived from existing data with some confidence.
     */
    Medium,

    /**
     * Derived from existing data with reasonably high confidence.
     */
    High
}

/**
 * UV protection recommendation.
 */
@Stable
enum class UvRecommendation {
    NoProtection,
    LowProtection,
    MediumProtection,
    HighProtection,
}

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
