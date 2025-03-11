package com.youllbecold.trustme.log.history.ui.model

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.youllbecold.trustme.log.ui.model.LogState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * UI state for the history screen.
 */
@Stable
data class HistoryUiState(
   val logs: Flow<PagingData<LogState>> = flow {
       emit(PagingData.empty<LogState>())
   }
)
