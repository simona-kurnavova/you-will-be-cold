package com.youllbecold.trustme.log.edit.ui.model

import com.youllbecold.trustme.common.ui.model.log.LogState

/**
 * UI state for the add log screen.
 */
data class EditLogUiState(
    val logState: LogState?,
    val editState: EditingState = EditingState.None,
)

enum class EditingState {
    None,
    Updated,
    Error
}
