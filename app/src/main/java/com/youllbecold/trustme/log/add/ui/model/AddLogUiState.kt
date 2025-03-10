package com.youllbecold.trustme.log.add.ui.model

import com.youllbecold.trustme.common.ui.model.log.LogState

/**
 * UI state for the add log screen.
 */
data class AddLogUiState(
    val logState: LogState,
    val saveState: SavingState = SavingState.None,
)

enum class SavingState {
    None,
    Saved,
    Error
}
