package com.youllbecold.trustme.log.ui.model

import androidx.compose.runtime.Stable
import com.youllbecold.logdatabase.model.Feeling
import com.youllbecold.logdatabase.model.Feelings
import com.youllbecold.trustme.common.ui.components.utils.DateTimeState
import com.youllbecold.trustme.common.ui.model.clothes.Clothes
import com.youllbecold.trustme.log.ui.mappers.toFeelingsWithLabel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf
import java.time.LocalDate
import java.time.LocalTime

/**
 * Log state.
 */
@Stable
data class LogState(
    val id: Int? = null,
    val dateTimeState: DateTimeState,
    val feelings: PersistentList<FeelingWithLabel> = initialiseFeelings(),
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

/**
 * Weather parameters.
 */
@Stable
data class WeatherParams(
    val apparentTemperatureMin: Double,
    val apparentTemperatureMax: Double,
    val avgTemperature: Double,
    val useCelsiusUnits: Boolean
)

/**
 * Returns the worst feeling in the state.
 */
val PersistentList<FeelingWithLabel>.worstFeeling: FeelingState
    get() = filter { it.feeling != FeelingState.NORMAL }
        .minByOrNull { it.feeling.ordinal }?.feeling // Consider cold worst than hot.
        ?: FeelingState.NORMAL

private fun initialiseFeelings(): PersistentList<FeelingWithLabel> =
    Feelings(
        head = Feeling.NORMAL,
        neck = Feeling.NORMAL,
        top = Feeling.NORMAL,
        bottom = Feeling.NORMAL,
        hand = Feeling.NORMAL,
        feet = Feeling.NORMAL
    ).toFeelingsWithLabel()