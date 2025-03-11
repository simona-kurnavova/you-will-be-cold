package com.youllbecold.trustme.settings.ui.model

import androidx.compose.runtime.Stable

/**
 * UI state for settings screen.
 */
@Stable
data class SettingsUiState(
    val allowDailyNotification: Boolean = false,
    val allowRecommendNotification: Boolean = false,
    val useCelsiusUnits: Boolean = false
)
