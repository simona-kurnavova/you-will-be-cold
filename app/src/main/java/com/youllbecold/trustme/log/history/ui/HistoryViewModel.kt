package com.youllbecold.trustme.log.history.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.youllbecold.trustme.common.domain.units.UnitsManager
import com.youllbecold.trustme.log.history.ui.model.DeleteStatus
import com.youllbecold.trustme.log.history.ui.model.HistoryUiState
import com.youllbecold.trustme.log.history.usecases.DeleteLogUseCase
import com.youllbecold.trustme.log.history.usecases.FetchAllLogsUseCase
import com.youllbecold.trustme.log.ui.model.LogState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

/**
 * ViewModel for the history screen.
 */
@KoinViewModel
class HistoryViewModel(
    private val deleteLogUseCase: DeleteLogUseCase,
    private val fetchAllLogsUseCase: FetchAllLogsUseCase,
    unitsManager: UnitsManager
) : ViewModel() {
    private val deleteStatus: MutableStateFlow<DeleteStatus> = MutableStateFlow(DeleteStatus.Idle)

    /**
     * UI state for the history screen.
     */
    val uiState: StateFlow<HistoryUiState> = combine(
        deleteStatus,
        unitsManager.unitsCelsius
    ) { deleteStatus, useCelsius ->
        HistoryUiState(
            logs = fetchAllLogsUseCase
                .fetchAllLogs(useCelsius)
                .cachedIn(viewModelScope),
            deleteStatus = deleteStatus
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, HistoryUiState())

    fun onAction(action: HistoryAction) {
        when (action) {
            is HistoryAction.Delete -> deleteLog(action.state)
            is HistoryAction.Edit -> Unit // Do nothing, will be handled by the navigation
        }
    }

    private fun deleteLog(logState: LogState) {
        viewModelScope.launch {
            val successful = deleteLogUseCase.deleteLog(logState)
            deleteStatus.update {
                if (successful) DeleteStatus.Success else DeleteStatus.Error
            }
        }
    }
}

/**
 * Actions for the history screen.
 */
sealed class HistoryAction {
    data class Edit(val state: LogState) : HistoryAction()
    data class Delete(val state: LogState) : HistoryAction()
}
