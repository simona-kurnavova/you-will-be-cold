package com.youllbecold.trustme.log.ui.mappers

import com.youllbecold.logdatabase.model.Feeling
import com.youllbecold.logdatabase.model.Feelings
import com.youllbecold.logdatabase.model.LogData
import com.youllbecold.logdatabase.model.WeatherData
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.components.utils.DateTimeState
import com.youllbecold.trustme.common.ui.mappers.toClothes
import com.youllbecold.trustme.log.ui.model.BodyPart
import com.youllbecold.trustme.log.ui.model.FeelingState
import com.youllbecold.trustme.log.ui.model.FeelingWithLabel
import com.youllbecold.trustme.log.ui.model.LogState
import com.youllbecold.trustme.log.ui.model.WeatherParams
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentSet

/**
 * Maps a [LogData] to a [LogState].
 */
fun LogData.toLogState(
    useCelsiusUnits: Boolean = true
): LogState = LogState(
    id = id,
    dateTimeState = DateTimeState.fromDateTime(
        dateTimeFrom = dateFrom,
        datetimeTo = dateTo
    ),
    feelings = feelings.toFeelingsWithLabel(),
    clothes = clothes.map { it.toClothes() }.toPersistentSet(),
    weather = weatherData.toWeatherState(useCelsiusUnits)
)

fun Feelings.toFeelingsWithLabel(): PersistentList<FeelingWithLabel> =
    persistentListOf(
        FeelingWithLabel(
            bodyPart = BodyPart.HEAD,
            feeling = head.toFeelingState(),
            label = R.string.label_head,
        ),
        FeelingWithLabel(
            bodyPart = BodyPart.NECK,
            feeling = neck.toFeelingState(),
            label = R.string.label_neck,
        ),
        FeelingWithLabel(
            bodyPart = BodyPart.TOP,
            feeling = top.toFeelingState(),
            label = R.string.label_top,
        ),
        FeelingWithLabel(
            bodyPart = BodyPart.BOTTOM,
            feeling = bottom.toFeelingState(),
            label = R.string.label_bottom
        ),
        FeelingWithLabel(
            bodyPart = BodyPart.FEET,
            feeling = feet.toFeelingState(),
            label = R.string.label_feet
        ),
        FeelingWithLabel(
            bodyPart = BodyPart.HANDS,
            feeling = hand.toFeelingState(),
            label = R.string.label_hands
        )
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
