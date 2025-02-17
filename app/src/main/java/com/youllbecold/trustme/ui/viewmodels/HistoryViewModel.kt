package com.youllbecold.trustme.ui.viewmodels

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.logdatabase.model.LogData
import com.youllbecold.logdatabase.model.Feeling
import com.youllbecold.logdatabase.model.Feelings
import com.youllbecold.logdatabase.model.WeatherData
import com.youllbecold.trustme.ui.components.utils.ImmutableDate
import com.youllbecold.trustme.ui.components.utils.ImmutableTime
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

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
            logs = logs.map { it.toLogState() }
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, HistoryUiState())

    fun onAction(action: HistoryAction) {
        when (action) {
            is HistoryAction.Delete -> {
                viewModelScope.launch {
                    logRepository.deleteLog(action.state.toLogData())
                }
            }
            is HistoryAction.Edit -> Unit // Do nothing, will be handled by the navigation
        }
    }

    private fun LogData.toLogState(): LogState = LogState(
        id = id,
        date = ImmutableDate(dateFrom.toLocalDate()),
        timeFrom = ImmutableTime(dateFrom.toLocalTime()),
        timeTo = ImmutableTime(dateTo.toLocalTime()),
        feelings = feelings.toFeelingsState(),
        clothes = clothes.toPersistentSet(),
        weather = weatherData?.toWeatherState()
    )

    private fun Feelings.toFeelingsState(): FeelingsState = FeelingsState(
        head = head.toFeelingState(),
        neck = neck.toFeelingState(),
        top = top.toFeelingState(),
        bottom = bottom.toFeelingState(),
        feet = feet.toFeelingState(),
        hand = hand.toFeelingState()
    )

    private fun Feeling.toFeelingState(): FeelingState = when (this) {
        Feeling.VERY_COLD -> FeelingState.VERY_COLD
        Feeling.COLD -> FeelingState.COLD
        Feeling.NORMAL -> FeelingState.NORMAL
        Feeling.WARM -> FeelingState.WARM
        Feeling.VERY_WARM -> FeelingState.VERY_WARM
    }

    private fun WeatherData.toWeatherState(): WeatherState = WeatherState(
        apparentTemperatureMin = apparentTemperatureMin,
        apparentTemperatureMax = apparentTemperatureMax,
        avgTemperature = avgTemperature
    )
}

/**
 * UI state for the history screen.
 */
@Immutable
data class HistoryUiState(
    val logs: List<LogState> = emptyList()
)

/**
 * Actions for the history screen.
 */
sealed class HistoryAction {
    data class Edit(val state: LogState) : HistoryAction()
    data class Delete(val state: LogState) : HistoryAction()
}
