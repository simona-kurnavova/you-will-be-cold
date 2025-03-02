package com.youllbecold.trustme.ui.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.trustme.ui.components.utils.DateTimeState
import com.youllbecold.trustme.ui.viewmodels.state.LogState
import com.youllbecold.trustme.ui.viewmodels.state.toLogData
import com.youllbecold.trustme.usecases.weather.RangedWeatherUseCase
import com.youllbecold.trustme.utils.LocationHelper
import com.youllbecold.trustme.utils.PermissionHelper
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
    private val locationHelper: LocationHelper
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
        if (!PermissionHelper.hasLocationPermission(app)) {
            saveState.value = SavingState.Error
            return
        }

        viewModelScope.launch {
            val location = locationHelper.geoLocationState.firstOrNull()?.location
                ?: LocationHelper.getLastLocation(app)

            if (location == null) {
                saveState.value = SavingState.Error
                return@launch
            }

            val weather = weatherUseCase.obtainRangedWeatherState(
                location = location,
                date = logState.dateTimeState.date.date,
                timeFrom = logState.dateTimeState.timeFrom.time,
                timeTo = logState.dateTimeState.timeTo.time,
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

/**
 * UI state for the add log screen.
 */
data class AddLogUiState(
    val logState: LogState,
    val saveState: SavingState = SavingState.None,
)

enum class SavingState {
    None,
    Saved,
    Error
}