package com.youllbecold.trustme.log.ui.model.mappers

import com.youllbecold.logdatabase.model.Feeling
import com.youllbecold.logdatabase.model.Feelings
import com.youllbecold.logdatabase.model.LogData
import com.youllbecold.logdatabase.model.WeatherData
import com.youllbecold.trustme.common.ui.components.utils.DateTimeState
import com.youllbecold.trustme.common.ui.mappers.toClothes
import com.youllbecold.trustme.log.ui.model.FeelingState
import com.youllbecold.trustme.log.ui.model.FeelingsState
import com.youllbecold.trustme.log.ui.model.LogState
import com.youllbecold.trustme.log.ui.model.WeatherParams
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
    clothes = clothes.map { it.toClothes() }.toPersistentSet(),
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
