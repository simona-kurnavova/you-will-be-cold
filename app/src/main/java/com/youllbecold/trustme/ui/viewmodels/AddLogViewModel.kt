package com.youllbecold.trustme.ui.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.trustme.ui.components.utils.ImmutableDate
import com.youllbecold.trustme.ui.components.utils.ImmutableTime
import com.youllbecold.trustme.usecases.weather.RangedWeatherUseCase
import com.youllbecold.trustme.utils.LocationHelper
import com.youllbecold.trustme.utils.PermissionHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.time.LocalDate
import java.time.LocalTime

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
    }.stateIn(viewModelScope, SharingStarted.Eagerly, AddLogUiState(logState.value))

    /**
     * Handles [AddLogAction].
     */
    fun onAction(action: AddLogAction) {
        when (action) {
            is AddLogAction.SaveProgress -> logState.value = action.state
            is AddLogAction.SaveLog -> saveLogWithWeather(logState.value)
            else -> Unit // Handled in the screen
        }
    }

    private fun initialiseState(): LogState {
        val currentTime = LocalTime.now()

        // Create initial log with default values.
        return LogState(
            date = ImmutableDate(LocalDate.now()),
            timeFrom = ImmutableTime(currentTime.minusHours(1)),
            timeTo = ImmutableTime(currentTime)
        )
    }

    @SuppressLint("MissingPermission") // Permission is checked
    private fun saveLogWithWeather(logState: LogState) {
        if (!PermissionHelper.hasLocationPermission(app)) {
            saveState.value = SavingState.Error
            return
        }

        viewModelScope.launch {
            val geoLocationState = locationHelper.geoLocationState.firstOrNull()
            if (geoLocationState?.location == null) {
                saveState.value = SavingState.Error
                return@launch
            }

            val weatherState = weatherUseCase.obtainRangedWeather(
                geoLocationState.location,
                logState.date.date,
                logState.timeFrom.time,
                logState.timeTo.time
            )

            val weather = weatherState.getOrNull()

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