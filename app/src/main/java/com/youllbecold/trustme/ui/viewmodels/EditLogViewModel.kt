package com.youllbecold.trustme.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.logdatabase.api.LogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class EditLogViewModel(
    private val logRepository: LogRepository,
) : ViewModel() {
    private var oldLogState: LogState? = null

    private val _state: MutableStateFlow<LogState?> = MutableStateFlow(null)
    val state: StateFlow<LogState?> = _state

    /**
     * Handles [EditLogAction].
     */
    fun onAction(action: EditLogAction) {
        when (action) {
            is EditLogAction.StartEdit -> startEdit(action.id)
            is EditLogAction.SaveProgress -> _state.value = action.state
            is EditLogAction.SaveLog -> state.value?.let { saveLog(it) }
        }
    }

    private fun startEdit(id: Int) {
        viewModelScope.launch {
            val log = logRepository.getLog(id)
            oldLogState = log?.toLogState()
            _state.value = oldLogState
        }
    }

    private fun saveLog(logData: LogState) {
        if (oldLogState?.date != logData.date ||
            oldLogState?.timeFrom != logData.timeFrom ||
            oldLogState?.timeTo != logData.timeTo) {
            // TODO: update weather
        }

        viewModelScope.launch {
            logRepository.updateLog(logData.toLogData())
        }
    }
}

sealed class EditLogAction {
    data class StartEdit(val id: Int) : EditLogAction()
    data class SaveProgress(val state: LogState) : EditLogAction()
    data object SaveLog : EditLogAction()
}
