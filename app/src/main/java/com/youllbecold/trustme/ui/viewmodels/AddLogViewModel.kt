package com.youllbecold.trustme.ui.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.logdatabase.model.Feeling
import com.youllbecold.logdatabase.model.Feelings
import com.youllbecold.logdatabase.model.LogData
import com.youllbecold.logdatabase.model.WeatherData
import com.youllbecold.trustme.ui.components.utils.ImmutableDate
import com.youllbecold.trustme.ui.components.utils.ImmutableTime
import com.youllbecold.trustme.usecases.weather.RangedWeatherUseCase
import com.youllbecold.trustme.utils.LocationHelper
import com.youllbecold.trustme.utils.PermissionHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.time.LocalDate
import java.time.LocalTime

@KoinViewModel
class AddLogViewModel(
    private val app: Application,
    private val logRepository: LogRepository,
    private val weatherUseCase: RangedWeatherUseCase,
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

        // Create initial log with default values.
        return LogState(
            data = ImmutableDate(LocalDate.now()),
            timeFrom = ImmutableTime(currentTime.minusHours(1)),
            timeTo = ImmutableTime(currentTime),
            feelings = FeelingsState(),
            clothes = emptySet()
        )
    }

    private fun saveLog(logData: LogState) {
        viewModelScope.launch {
            val weatherData = WeatherData(
                apparentTemperatureMin = 25.0,
                apparentTemperatureMax = 25.0,
                avgTemperature = 25.0
            )

            // TODO: Obtain weather data.

            logRepository.addLog(logData.toLogData(weatherData))
        }
    }

    private fun LogState.toLogData(weatherData: WeatherData): LogData =
        LogData(
            dateFrom = data.date.atTime(timeFrom.time),
            dateTo = data.date.atTime(timeFrom.time),
            feelings = Feelings(
                head = feelings.head.toFeeling(),
                neck = feelings.neck.toFeeling(),
                top = feelings.top.toFeeling(),
                bottom = feelings.bottom.toFeeling(),
                feet = feelings.feet.toFeeling(),
                hand = feelings.hand.toFeeling()
            ),
            weatherData = weatherData,
            clothes = clothes.map { it }
        )

    private fun FeelingState.toFeeling(): Feeling = when (this) {
        FeelingState.NORMAL -> Feeling.NORMAL
        FeelingState.COLD -> Feeling.COLD
        FeelingState.VERY_COLD -> Feeling.VERY_COLD
        FeelingState.WARM -> Feeling.WARM
        FeelingState.VERY_WARM -> Feeling.VERY_WARM
    }

    @SuppressLint("MissingPermission") // Permission is checked
    private fun obtainWeather(
        data: ImmutableDate,
        timeFrom: ImmutableTime,
        timeTo: ImmutableTime,
    ) {
        if (PermissionHelper.hasLocationPermission(app)) {
            LocationHelper.refreshLocation(
                app,
                onSuccess = { location ->
                    viewModelScope.launch {
                        val result = weatherUseCase.obtainRangedWeather(
                            location,
                            data.date,
                            timeFrom.time,
                            timeTo.time
                        )

                        // TODO: Use result.
                    }
                },
                onError = { }
            )
        }
    }
}

sealed class AddLogAction {
    data class SaveProgress(val state: LogState) : AddLogAction()
    data object SaveLog : AddLogAction()
}

@Stable
data class LogState(
    val data: ImmutableDate,
    val timeFrom: ImmutableTime,
    val timeTo: ImmutableTime,
    val feelings: FeelingsState,
    val clothes: Set<Clothes>
)

enum class FeelingState {
    VERY_WARM,
    WARM,
    NORMAL,
    COLD,
    VERY_COLD,
}

data class FeelingsState(
    val head: FeelingState = FeelingState.NORMAL,
    val neck: FeelingState = FeelingState.NORMAL,
    val top: FeelingState = FeelingState.NORMAL,
    val bottom: FeelingState = FeelingState.NORMAL,
    val feet: FeelingState = FeelingState.NORMAL,
    val hand: FeelingState = FeelingState.NORMAL,
)