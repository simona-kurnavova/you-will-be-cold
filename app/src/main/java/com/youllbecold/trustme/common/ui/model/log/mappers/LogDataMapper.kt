package com.youllbecold.trustme.common.ui.model.log.mappers

import com.youllbecold.logdatabase.model.Feeling
import com.youllbecold.logdatabase.model.Feelings
import com.youllbecold.logdatabase.model.LogData
import com.youllbecold.logdatabase.model.WeatherData
import com.youllbecold.trustme.common.ui.components.utils.DateTimeState
import com.youllbecold.trustme.common.ui.model.log.FeelingState
import com.youllbecold.trustme.common.ui.model.log.FeelingsState
import com.youllbecold.trustme.common.ui.model.log.LogState
import com.youllbecold.trustme.common.ui.model.log.WeatherParams
import kotlinx.collections.immutable.toPersistentSet

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

private fun WeatherData.toWeatherState(useCelsiusUnits: Boolean): WeatherParams = WeatherParams(
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

private fun celsiusToFahrenheit(celsius: Double): Double =
    (celsius * 9 / 5) + 32
