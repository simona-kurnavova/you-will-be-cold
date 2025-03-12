package com.youllbecold.trustme.log.ui.mappers

import com.youllbecold.logdatabase.model.Feeling
import com.youllbecold.logdatabase.model.Feelings
import com.youllbecold.logdatabase.model.LogData
import com.youllbecold.logdatabase.model.WeatherData
import com.youllbecold.trustme.common.ui.mappers.toClothesModel
import com.youllbecold.trustme.log.ui.model.BodyPart
import com.youllbecold.trustme.log.ui.model.FeelingState
import com.youllbecold.trustme.log.ui.model.FeelingWithLabel
import com.youllbecold.trustme.log.ui.model.LogState
import com.youllbecold.trustme.log.ui.model.WeatherParams
import kotlinx.collections.immutable.PersistentList

fun LogState.toLogData(): LogData =
    LogData(
        id = id,
        dateFrom = date.atTime(timeFrom),
        dateTo = date.atTime(timeTo),
        feelings = feelings.toFeelings(),
        weatherData = weather?.toWeatherData()
            ?: throw IllegalStateException("Weather data is missing"),
        clothes = clothes.mapNotNull { it.toClothesModel() }
    )

private fun FeelingState.toFeeling(): Feeling = when (this) {
    FeelingState.NORMAL -> Feeling.NORMAL
    FeelingState.COLD -> Feeling.COLD
    FeelingState.VERY_COLD -> Feeling.VERY_COLD
    FeelingState.WARM -> Feeling.WARM
    FeelingState.VERY_WARM -> Feeling.VERY_WARM
}

private fun PersistentList<FeelingWithLabel>.toFeelings(): Feelings {
    val feelingsMap = this.associateBy { it.bodyPart }

    return Feelings(
        head = feelingsMap[BodyPart.HEAD]
            ?.feeling?.toFeeling() ?: Feeling.NORMAL,
        neck = feelingsMap[BodyPart.NECK]
            ?.feeling?.toFeeling() ?: Feeling.NORMAL,
        top = feelingsMap[BodyPart.TOP]
            ?.feeling?.toFeeling() ?: Feeling.NORMAL,
        bottom = feelingsMap[BodyPart.BOTTOM]
            ?.feeling?.toFeeling() ?: Feeling.NORMAL,
        feet = feelingsMap[BodyPart.FEET]
            ?.feeling?.toFeeling() ?: Feeling.NORMAL,
        hand = feelingsMap[BodyPart.HANDS]
            ?.feeling?.toFeeling() ?: Feeling.NORMAL
    )
}

private fun WeatherParams.toWeatherData(): WeatherData = WeatherData(
    apparentTemperatureMinC = apparentTemperatureMin,
    apparentTemperatureMaxC = apparentTemperatureMax,
    avgTemperatureC = avgTemperature,
)
