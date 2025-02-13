package com.youllbecold.trustme.ui.viewmodels

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.logdatabase.model.Feeling
import com.youllbecold.logdatabase.model.LogData
import com.youllbecold.logdatabase.model.WeatherData
import com.youllbecold.trustme.ui.components.utils.ImmutableDate
import com.youllbecold.trustme.ui.components.utils.ImmutableTime
import com.youllbecold.weather.api.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.time.LocalDate
import java.time.LocalTime

@KoinViewModel
class AddLogViewModel(
    private val logRepository: LogRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    private val _state: MutableStateFlow<LogState> = MutableStateFlow(initialiseState())
    val state: StateFlow<LogState> = _state

    /**
     * Handles [AddLogAction].
     */
    fun onAction(action: AddLogAction) {
        when (action) {
            is AddLogAction.SaveProgress -> _state.value = action.state
            is AddLogAction.SaveLog -> saveLog(state.value)
        }
    }

    private fun initialiseState(): LogState {
        val currentTime = LocalTime.now()

        return LogState(
            data = ImmutableDate(LocalDate.now()),
            timeFrom = ImmutableTime(currentTime.minusHours(1)),
            timeTo = ImmutableTime(currentTime),
            overallFeeling = null,
            clothes = emptySet()
        )
    }

    private fun saveLog(logData: LogState) {
        viewModelScope.launch {
           // TODO: Get actual weather data from repository.
            val weatherData = WeatherData(
                apparentTemperatureMin = 25.0,
                apparentTemperatureMax = 25.0,
                avgTemperature = 25.0
            )

            logRepository.addLog(logData.toLogData(weatherData))
        }
    }

    private fun LogState.toLogData(weatherData: WeatherData): LogData =
        LogData(
            dateFrom = data.date.atTime(timeFrom.time),
            dateTo = data.date.atTime(timeFrom.time),
            overallFeeling = when (overallFeeling) {
                null,
                FeelingState.NORMAL -> Feeling.NORMAL
                FeelingState.COLD -> Feeling.COLD
                FeelingState.VERY_COLD -> Feeling.VERY_COLD
                FeelingState.WARM -> Feeling.WARM
                FeelingState.VERY_WARM -> Feeling.VERY_WARM
            },
            weatherData = weatherData,
            clothes = clothes.map { it }
        )
}

sealed class AddLogAction {
    data class SaveProgress(val state: LogState) : AddLogAction()
    data object SaveLog : AddLogAction()
}

@Immutable
data class LogState(
    val data: ImmutableDate,
    val timeFrom: ImmutableTime,
    val timeTo: ImmutableTime,
    val overallFeeling: FeelingState?,
    val clothes: Set<Clothes>
)

enum class FeelingState {
    VERY_WARM,
    WARM,
    NORMAL,
    COLD,
    VERY_COLD,
}
