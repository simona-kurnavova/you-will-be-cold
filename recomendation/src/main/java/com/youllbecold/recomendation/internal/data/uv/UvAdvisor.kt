package com.youllbecold.recomendation.internal.data.uv

import com.youllbecold.recomendation.model.UvRecommendation

/**
 * Advisor class for UV index recommendations.
 */
internal object UvAdvisor {
    /**
     * Returns the UV index category based on the given UV index.
     *
     * @param uvIndex The UV indices hourly list.
     * @return The UV recommendation category.
     */
    fun uvRecommendation(uvIndex: List<Double>): UvRecommendation {
        val maxIndex = uvIndex.maxOrNull()
            ?: return UvRecommendation.LowProtection // Well, just in case

        return when {
            maxIndex <= 0 -> UvRecommendation.NoProtection
            maxIndex < 3 -> UvRecommendation.LowProtection
            maxIndex < 6 -> UvRecommendation.MediumProtection
            else -> UvRecommendation.HighProtection
        }
    }
}
