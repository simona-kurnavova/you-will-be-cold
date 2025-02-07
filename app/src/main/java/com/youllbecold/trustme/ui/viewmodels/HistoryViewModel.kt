package com.youllbecold.trustme.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.logdatabase.model.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
    val uiState: Flow<HistoryUiState> = logRepository.logs.map { logs ->
        HistoryUiState(
            logs = logs
        )
    }

    init {
        // TODO: Just for testing, remove when logic implemented.
        viewModelScope.launch {
            logRepository.addLog(
                Log(
                    id = null,
                    date = LocalDateTime.now(),
                    overallFeeling = Log.Feeling.WARM,
                    weatherData = Log.WeatherData(
                        temperature = 25.0,
                        apparentTemperature = 25.0,
                    )

                )
            )
        }
    }
}

/**
 * UI state for the history screen.
 */
data class HistoryUiState(
    val logs: List<Log> = emptyList()
)
