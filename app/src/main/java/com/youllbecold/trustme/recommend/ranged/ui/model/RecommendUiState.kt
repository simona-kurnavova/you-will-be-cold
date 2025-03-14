package com.youllbecold.trustme.recommend.ranged.ui.model

import androidx.compose.runtime.Stable
import com.youllbecold.trustme.common.ui.model.status.Idle
import com.youllbecold.trustme.common.ui.model.status.Status
import com.youllbecold.trustme.recommend.ui.model.WeatherWithRecommendation

/**
 * UI state for the recommendation screen.
 */
@Stable
data class RecommendUiState(
    val weatherWithRecommendation: WeatherWithRecommendation? = null,
    val status: Status = Idle,
)
