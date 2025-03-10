package com.youllbecold.trustme.settings.ui.model

/**
 * UI state for settings screen.
 */
data class SettingsUiState(
    val allowDailyNotification: Boolean = false,
    val allowRecommendNotification: Boolean = false,
    val useCelsiusUnits: Boolean = false
)
