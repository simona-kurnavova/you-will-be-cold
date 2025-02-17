package com.youllbecold.trustme.ui.viewmodels

import androidx.compose.runtime.Stable
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.logdatabase.model.Feeling
import com.youllbecold.logdatabase.model.Feelings
import com.youllbecold.logdatabase.model.LogData
import com.youllbecold.logdatabase.model.WeatherData
import com.youllbecold.trustme.ui.components.utils.ImmutableDate
import com.youllbecold.trustme.ui.components.utils.ImmutableTime
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf

@Stable
data class LogState(
    val id: Int? = null,
    val date: ImmutableDate,
    val timeFrom: ImmutableTime,
    val timeTo: ImmutableTime,
    val feelings: FeelingsState = FeelingsState(),
    val clothes: PersistentSet<Clothes> = persistentSetOf(),
    val weather: WeatherState? = null
)

enum class FeelingState {
    VERY_COLD,
    COLD,
    NORMAL,
    WARM,
    VERY_WARM,
}

data class FeelingsState(
    val head: FeelingState = FeelingState.NORMAL,
    val neck: FeelingState = FeelingState.NORMAL,
    val top: FeelingState = FeelingState.NORMAL,
    val bottom: FeelingState = FeelingState.NORMAL,
    val feet: FeelingState = FeelingState.NORMAL,
    val hand: FeelingState = FeelingState.NORMAL,
)

data class WeatherState(
    val apparentTemperatureMin: Double,
    val apparentTemperatureMax: Double,
    val avgTemperature: Double
)

fun LogState.toLogData(): LogData =
    LogData(
        id = id,
        dateFrom = date.date.atTime(timeFrom.time),
        dateTo = date.date.atTime(timeFrom.time),
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
    apparentTemperatureMin = apparentTemperatureMin,
    apparentTemperatureMax = apparentTemperatureMax,
    avgTemperature = avgTemperature
)