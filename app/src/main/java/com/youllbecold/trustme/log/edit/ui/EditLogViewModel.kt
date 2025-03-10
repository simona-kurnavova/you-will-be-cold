package com.youllbecold.trustme.log.edit.ui

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.trustme.common.data.location.LocationController
import com.youllbecold.trustme.common.data.permissions.PermissionChecker
import com.youllbecold.trustme.common.domain.usecases.weather.RangedWeatherUseCase
import com.youllbecold.trustme.common.ui.model.log.LogState
import com.youllbecold.trustme.common.ui.model.log.mappers.toLogData
import com.youllbecold.trustme.common.ui.model.log.mappers.toLogState
import com.youllbecold.trustme.log.edit.ui.model.EditLogUiState
import com.youllbecold.trustme.log.edit.ui.model.EditingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class EditLogViewModel(
    private val app: Application,
    private val logRepository: LogRepository,
    private val locationController: LocationController,
    private val weatherUseCase: RangedWeatherUseCase,
) : ViewModel() {
    private var oldLogState: LogState? = null

    private val editState: MutableStateFlow<EditingState> = MutableStateFlow(EditingState.None)
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
            val log = logRepository.getLog(id)
            oldLogState = log?.toLogState()
            logState.update { oldLogState }
        }
    }

    @SuppressLint("MissingPermission") // Permission is checked
    private fun saveLog(logState: LogState) {
        if (!PermissionChecker.hasLocationPermission(app)) {
            editState.update { EditingState.Error }
            return
        }

        viewModelScope.launch {
            var log = logState

            if (oldLogState?.date != logState.date ||
                oldLogState?.timeFrom != logState.timeFrom ||
                oldLogState?.timeTo != logState.timeTo
            ) {
                val location = locationController.geoLocationState.firstOrNull()?.location
                    ?: LocationController.getLastLocation(app)

                if (location == null) {
                    editState.update { EditingState.Error }
                    return@launch
                }

                val weatherState = weatherUseCase.obtainRangedWeatherState(
                    location = location,
                    date = logState.dateTimeState.date.localDate,
                    timeFrom = logState.dateTimeState.timeFrom.localTime,
                    timeTo = logState.dateTimeState.timeTo.localTime,
                    useCelsiusUnits = true
                )

                val weather = weatherState.getOrNull()

                if (weather == null) {
                    Log.d("AddLogViewModel", "Failed to get weather data")
                    editState.update { EditingState.Error }
                    return@launch
                }
                log = logState.copy(weather = weather)
            }

            logRepository.updateLog(log.toLogData())
            editState.update { EditingState.Updated }
        }
    }
}

sealed class EditLogAction {
    data class StartEdit(val id: Int) : EditLogAction()
    data class SaveProgress(val state: LogState) : EditLogAction()
    data object SaveLog : EditLogAction()
    data object ExitForm : EditLogAction()
}
