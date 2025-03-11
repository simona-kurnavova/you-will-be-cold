package com.youllbecold.trustme.log.add.ui.model

import androidx.compose.runtime.Stable
import com.youllbecold.trustme.log.ui.model.LogState
import com.youllbecold.trustme.common.ui.model.status.LoadingStatus

/**
 * UI state for the add log screen.
 */
@Stable
data class AddLogUiState(
    val logState: LogState,
    val saveState: LoadingStatus = LoadingStatus.Idle,
)
