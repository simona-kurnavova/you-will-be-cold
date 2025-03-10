package com.youllbecold.trustme.log.add.ui

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.trustme.log.add.ui.model.AddLogUiState
import com.youllbecold.trustme.log.add.ui.model.SavingState
import com.youllbecold.trustme.common.data.location.LocationController
import com.youllbecold.trustme.common.data.permissions.PermissionChecker
import com.youllbecold.trustme.common.domain.usecases.weather.RangedWeatherUseCase
import com.youllbecold.trustme.common.ui.components.utils.DateTimeState
import com.youllbecold.trustme.common.ui.model.log.LogState
import com.youllbecold.trustme.common.ui.model.log.mappers.toLogData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.time.LocalDateTime

@KoinViewModel
class AddLogViewModel(
    private val app: Application,
    private val logRepository: LogRepository,
    private val weatherUseCase: RangedWeatherUseCase,
    private val locationController: LocationController
) : ViewModel() {
    private val saveState: MutableStateFlow<SavingState> = MutableStateFlow(SavingState.None)
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

    @SuppressLint("MissingPermission") // Permission is checked
    private fun saveLogWithWeather(logState: LogState) {
        if (!PermissionChecker.hasLocationPermission(app)) {
            saveState.value = SavingState.Error
            return
        }

        viewModelScope.launch {
            val location = locationController.geoLocationState.firstOrNull()?.location
                ?: LocationController.getLastLocation(app)

            if (location == null) {
                saveState.value = SavingState.Error
                return@launch
            }

            val weather = weatherUseCase.obtainRangedWeatherState(
                location = location,
                date = logState.dateTimeState.date.localDate,
                timeFrom = logState.dateTimeState.timeFrom.localTime,
                timeTo = logState.dateTimeState.timeTo.localTime,
                useCelsiusUnits = true // Always save with Celsius units
            ).getOrNull()

            if(weather == null) {
                Log.d("AddLogViewModel", "Failed to get weather data")
                saveState.value = SavingState.Error
                return@launch
            }

            logRepository.addLog(
                logState.copy(weather = weather).toLogData()
            )

            saveState.value = SavingState.Saved
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
