package com.youllbecold.trustme.common.ui.model.log

import androidx.compose.runtime.Stable
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.trustme.common.ui.components.utils.DateTimeState
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf
import java.time.LocalDate
import java.time.LocalTime

@Stable
data class LogState(
    val id: Int? = null,
    val dateTimeState: DateTimeState,
    val feelings: FeelingsState = FeelingsState(),
    val clothes: PersistentSet<Clothes> = persistentSetOf(),
    val weather: WeatherParams? = null
) {
    val date: LocalDate
        get() = dateTimeState.date.localDate

    val timeFrom: LocalTime
        get() = dateTimeState.timeFrom.localTime

    val timeTo: LocalTime
        get() = dateTimeState.timeTo.localTime
}

@Stable
data class WeatherParams(
    val apparentTemperatureMin: Double,
    val apparentTemperatureMax: Double,
    val avgTemperature: Double,
    val useCelsiusUnits: Boolean
)

@Stable
data class FeelingsState(
    val head: FeelingState = FeelingState.NORMAL,
    val neck: FeelingState = FeelingState.NORMAL,
    val top: FeelingState = FeelingState.NORMAL,
    val bottom: FeelingState = FeelingState.NORMAL,
    val feet: FeelingState = FeelingState.NORMAL,
    val hand: FeelingState = FeelingState.NORMAL,
)

enum class FeelingState {
    VERY_COLD,
    COLD,
    NORMAL,
    WARM,
    VERY_WARM,
}
