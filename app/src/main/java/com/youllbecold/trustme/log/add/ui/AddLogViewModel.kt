package com.youllbecold.trustme.log.add.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.trustme.common.ui.components.utils.DateTimeState
import com.youllbecold.trustme.common.ui.model.status.Idle
import com.youllbecold.trustme.common.ui.model.status.Loading
import com.youllbecold.trustme.log.ui.model.LogState
import com.youllbecold.trustme.common.ui.model.status.Status
import com.youllbecold.trustme.log.add.ui.model.AddLogUiState
import com.youllbecold.trustme.log.add.usecases.AddLogUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.time.LocalDateTime

@KoinViewModel
class AddLogViewModel(
    private val addLogUseCase: AddLogUseCase
) : ViewModel() {
    private val saveState: MutableStateFlow<Status> = MutableStateFlow(Idle)
    private val logState: MutableStateFlow<LogState> = MutableStateFlow(initialiseState())

    /**
     * UI state for the add log screen.
     */
    val state: StateFlow<AddLogUiState> = combine(
        logState,
        saveState
    ) { log, saveState ->
        AddLogUiState(
            logState = log,
            saveState = saveState
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, AddLogUiState(logState.value))

    /**
     * Handles [AddLogAction].
     */
    fun onAction(action: AddLogAction) {
        when (action) {
            is AddLogAction.SaveProgress -> logState.update { action.state }
            is AddLogAction.SaveLog -> saveLogWithWeather(logState.value)
            else -> Unit // Handled in the screen
        }
    }

    private fun initialiseState(): LogState {
        val currentDateTime = LocalDateTime.now()

        // Create initial log with default values.
        return LogState(
            dateTimeState = DateTimeState.fromDateTime(currentDateTime.minusHours(1), currentDateTime),
        )
    }

    private fun saveLogWithWeather(logState: LogState) {
        saveState.update { Loading }

        viewModelScope.launch {
            saveState.update { addLogUseCase.saveLogWithWeather(logState) }
        }
    }
}

/**
 * Actions that can be performed on the add log screen.
 */
sealed class AddLogAction {
    data class SaveProgress(val state: LogState) : AddLogAction()
    data object SaveLog : AddLogAction()
    data object ExitForm : AddLogAction()
}
