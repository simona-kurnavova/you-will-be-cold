package com.youllbecold.trustme.ui.viewmodels

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.logdatabase.model.LogData
import com.youllbecold.logdatabase.model.Feeling
import com.youllbecold.logdatabase.model.WeatherData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.time.LocalDateTime

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
            logs = logs
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, HistoryUiState())

    init {
        // TODO: Just for testing, remove when logic implemented.
        viewModelScope.launch {
            logRepository.addLog(
                LogData(
                    id = null,
                    dateFrom = LocalDateTime.now(),
                    dateTo = LocalDateTime.now(),
                    overallFeeling = Feeling.WARM,
                    weatherData = WeatherData(
                        apparentTemperatureMin = 25.0,
                        apparentTemperatureMax = 25.0,
                        avgTemperature = 25.0
                    ),
                    clothes = emptyList() // listOf(Clothes.DRESS)
                )
            )
        }
    }
}

/**
 * UI state for the history screen.
 */
@Immutable
data class HistoryUiState(
    val logs: List<LogData> = emptyList()
)
