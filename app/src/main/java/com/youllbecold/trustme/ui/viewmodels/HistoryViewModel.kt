package com.youllbecold.trustme.ui.viewmodels

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.logdatabase.api.LogRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

/**
 * ViewModel for the history screen.
 */
@KoinViewModel
class HistoryViewModel(
    private val logRepository: LogRepository
) : ViewModel() {

    /**
     * UI state for the history screen.
     */
    val uiState: StateFlow<HistoryUiState> = logRepository.logs.map { logs ->
        HistoryUiState(
            logs = logs.map { it.toLogState() }
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, HistoryUiState())

    fun onAction(action: HistoryAction) {
        when (action) {
            is HistoryAction.Delete -> {
                viewModelScope.launch {
                    logRepository.deleteLog(action.state.toLogData())
                }
            }
            is HistoryAction.Edit -> Unit // Do nothing, will be handled by the navigation
        }
    }
}

/**
 * UI state for the history screen.
 */
@Immutable
data class HistoryUiState(
    val logs: List<LogState> = emptyList()
)

/**
 * Actions for the history screen.
 */
sealed class HistoryAction {
    data class Edit(val state: LogState) : HistoryAction()
    data class Delete(val state: LogState) : HistoryAction()
}
