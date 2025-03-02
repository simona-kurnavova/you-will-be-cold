package com.youllbecold.trustme.ui.viewmodels.state

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Stable
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.logdatabase.model.Feeling
import com.youllbecold.logdatabase.model.Feelings
import com.youllbecold.logdatabase.model.LogData
import com.youllbecold.logdatabase.model.WeatherData
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.utils.DateTimeState
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toPersistentSet
import java.time.LocalDate
import java.time.LocalTime

@Stable
data class LogState(
    val id: Int? = null,
    val dateTimeState: DateTimeState,
    val feelings: FeelingsState = FeelingsState(),
    val clothes: PersistentSet<Clothes> = persistentSetOf(),
    val weather: WeatherState? = null
) {
    val date: LocalDate
        get() = dateTimeState.date.date

    val timeFrom: LocalTime
        get() = dateTimeState.timeFrom.time

    val timeTo: LocalTime
        get() = dateTimeState.timeTo.time
}

enum class FeelingState {
    VERY_COLD,
    COLD,
    NORMAL,
    WARM,
    VERY_WARM,
}

@Stable
data class FeelingsState(
    val head: FeelingState = FeelingState.NORMAL,
    val neck: FeelingState = FeelingState.NORMAL,
    val top: FeelingState = FeelingState.NORMAL,
    val bottom: FeelingState = FeelingState.NORMAL,
    val feet: FeelingState = FeelingState.NORMAL,
    val hand: FeelingState = FeelingState.NORMAL,
)

@Stable
data class WeatherState(
    val apparentTemperatureMin: Double,
    val apparentTemperatureMax: Double,
    val avgTemperature: Double,
    val useCelsiusUnits: Boolean
)

fun LogState.toLogData(): LogData =
    LogData(
        id = id,
        dateFrom = date.atTime(timeFrom),
        dateTo = date.atTime(timeTo),
        feelings = Feelings(
            head = feelings.head.toFeeling(),
            neck = feelings.neck.toFeeling(),
            top = feelings.top.toFeeling(),
            bottom = feelings.bottom.toFeeling(),
            feet = feelings.feet.toFeeling(),
            hand = feelings.hand.toFeeling()
        ),
        weatherData = weather?.toWeatherData()
            ?: throw IllegalStateException("Weather data is missing"),
        clothes = clothes.map { it }
    )

private fun FeelingState.toFeeling(): Feeling = when (this) {
    FeelingState.NORMAL -> Feeling.NORMAL
    FeelingState.COLD -> Feeling.COLD
    FeelingState.VERY_COLD -> Feeling.VERY_COLD
    FeelingState.WARM -> Feeling.WARM
    FeelingState.VERY_WARM -> Feeling.VERY_WARM
}

fun WeatherState.toWeatherData(): WeatherData = WeatherData(
    apparentTemperatureMinC = apparentTemperatureMin,
    apparentTemperatureMaxC = apparentTemperatureMax,
    avgTemperatureC = avgTemperature,
)

fun LogData.toLogState(
    useCelsiusUnits: Boolean = true
): LogState = LogState(
    id = id,
    dateTimeState = DateTimeState.fromDateTime(
        dateTimeFrom = dateFrom,
        datetimeTo = dateTo
    ),
    feelings = feelings.toFeelingsState(),
    clothes = clothes.toPersistentSet(),
    weather = weatherData.toWeatherState(useCelsiusUnits)
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

private fun WeatherData.toWeatherState(useCelsiusUnits: Boolean): WeatherState = WeatherState(
    apparentTemperatureMin = if (useCelsiusUnits) {
        apparentTemperatureMinC
    } else {
        celsiusToFahrenheit(apparentTemperatureMinC)
    },
    apparentTemperatureMax = if (useCelsiusUnits) {
        apparentTemperatureMaxC
    } else {
        celsiusToFahrenheit(apparentTemperatureMaxC)
    },
    avgTemperature = if (useCelsiusUnits) {
        avgTemperatureC
    } else {
        celsiusToFahrenheit(avgTemperatureC)
    },
    useCelsiusUnits = useCelsiusUnits
)

/**
 * Validates the log state and shows a toast if the state is invalid.
 */
fun LogState.validate(context: Context): Boolean {
    if (timeTo.isBefore(timeFrom) || timeTo == timeFrom) {
        Toast.makeText(
            context,
            context.getString(R.string.toast_invalid_time_range),
            Toast.LENGTH_SHORT
        ).show()
        return false
    }

    if (clothes.isEmpty()) {
        Toast.makeText(
            context,
            context.getString(R.string.toast_no_clothes_selected),
            Toast.LENGTH_SHORT
        ).show()
        return false
    }

    return true
}

private fun celsiusToFahrenheit(celsius: Double): Double =
    (celsius * 9 / 5) + 32
