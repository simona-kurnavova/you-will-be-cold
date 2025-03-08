package com.youllbecold.recomendation.model

import androidx.compose.runtime.Stable

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
