package com.youllbecold.trustme.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.logdatabase.model.Feeling
import com.youllbecold.logdatabase.model.LogData
import com.youllbecold.logdatabase.model.WeatherData
import com.youllbecold.weather.api.WeatherRepository
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.time.LocalDateTime
import java.time.LocalTime

@KoinViewModel
class AddLogViewModel(
    private val logRepository: LogRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    /**
     * Handles [AddLogAction].
     */
    fun onAction(action: AddLogAction) {
        when (action) {
            is AddLogAction.SaveLog -> saveLog(action.logData)
        }
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
            dateFrom = LocalDateTime.now()
                .withHour(timeFrom.hour)
                .withMinute(timeFrom.minute),
            dateTo = LocalDateTime.now()
                .withHour(timeTo.hour)
                .withMinute(timeTo.minute),
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
    data class SaveLog(val logData: LogState) : AddLogAction()
}

data class LogState(
    val timeFrom: LocalTime,
    val timeTo: LocalTime,
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
