package com.youllbecold.trustme.log.history.ui.model

import androidx.paging.PagingData
import com.youllbecold.trustme.log.ui.model.LogState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * UI state for the history screen.
 *
 * Note: Not stable, logs need to be collected as lazy paging items.
 */
data class HistoryUiState(
    val logs: Flow<PagingData<LogState>> = flow {
        emit(PagingData.empty<LogState>())
    },
    val deleteStatus: DeleteStatus = DeleteStatus.Idle
)

enum class DeleteStatus {
    Idle,
    Success,
    Error
}
