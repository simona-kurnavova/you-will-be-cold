package com.youllbecold.recomendation.model

import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.logdatabase.model.LogData

/**
 * Data class for recommendation.
 */
data class Recommendation(
    val clothes: List<Clothes> = emptyList(),
    val certainty: Certainty = Certainty.Low,
    val uvLevel: UvRecommendation,
    val rainLevel: RainRecommendation
)

/**
 * Certainty level of the recommendation.
 */
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
enum class UvRecommendation {
    NoProtection,
    LowProtection,
    MediumProtection,
    HighProtection,
}

/**
 * Rain level recommendation.
 */
enum class RainRecommendation {
    NoRain,
    LightRain,
    MediumRain,
    HeavyRain,
}
