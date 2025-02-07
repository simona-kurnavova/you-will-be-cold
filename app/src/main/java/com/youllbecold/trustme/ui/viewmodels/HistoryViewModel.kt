package com.youllbecold.trustme.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.logdatabase.model.Log
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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

    val logs: StateFlow<List<Log>>
        get() = logRepository.logs
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    init {
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
