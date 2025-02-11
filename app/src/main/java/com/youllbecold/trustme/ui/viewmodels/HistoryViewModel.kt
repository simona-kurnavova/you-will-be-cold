package com.youllbecold.trustme.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.logdatabase.model.LogData
import com.youllbecold.logdatabase.model.Feeling
import com.youllbecold.logdatabase.model.WeatherData
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
data class HistoryUiState(
    val logs: List<LogData> = emptyList()
)
