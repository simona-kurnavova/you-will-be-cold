package com.youllbecold.trustme.log.edit.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.trustme.log.ui.model.LogState
import com.youllbecold.trustme.common.ui.model.status.LoadingStatus
import com.youllbecold.trustme.log.edit.ui.model.EditLogUiState
import com.youllbecold.trustme.log.edit.usecases.EditLogUseCase
import com.youllbecold.trustme.log.edit.usecases.FetchLogUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi

@OptIn(ExperimentalAtomicApi::class)
@KoinViewModel
class EditLogViewModel(
    private val fetchLogUseCase: FetchLogUseCase,
    private val editLogUseCase: EditLogUseCase
) : ViewModel() {
    private var oldLogState = AtomicReference<LogState?>(null)

    private val editState: MutableStateFlow<LoadingStatus> = MutableStateFlow(LoadingStatus.Idle)
    private val logState: MutableStateFlow<LogState?> = MutableStateFlow(null)

    val state: StateFlow<EditLogUiState> = combine(
        logState,
        editState
    ) { log, editState ->
        EditLogUiState(
            logState = log,
            editState = editState
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, EditLogUiState(logState.value))

    /**
     * Handles [EditLogAction].
     */
    fun onAction(action: EditLogAction) {
        when (action) {
            is EditLogAction.StartEdit -> startEdit(action.id)
            is EditLogAction.SaveProgress -> logState.update { action.state }
            is EditLogAction.SaveLog -> logState.value?.let { saveLog(it) }
            else -> Unit // Handled in the screen
        }
    }

    private fun startEdit(id: Int) {
        viewModelScope.launch {
            if (oldLogState.load()?.id == id) {
                return@launch // Already loaded, must have been just config change/recreation
            }

            val log = fetchLogUseCase.fetchLog(id)
            logState.update { log }
            oldLogState.exchange(log)
        }
    }

    private fun saveLog(logState: LogState) {
        viewModelScope.launch {
            val oldLog = oldLogState.exchange(null)
            oldLog?.let { oldLog ->
                val result = editLogUseCase.updateLog(oldLog, logState)
                editState.update { result }
            }
        }
    }
}

sealed class EditLogAction {
    data class StartEdit(val id: Int) : EditLogAction()
    data class SaveProgress(val state: LogState) : EditLogAction()
    data object SaveLog : EditLogAction()
    data object ExitForm : EditLogAction()
}
