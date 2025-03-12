package com.youllbecold.trustme.log.ui.model.mappers

import com.youllbecold.logdatabase.model.Feeling
import com.youllbecold.logdatabase.model.Feelings
import com.youllbecold.logdatabase.model.LogData
import com.youllbecold.logdatabase.model.WeatherData
import com.youllbecold.trustme.common.ui.mappers.toClothesModel
import com.youllbecold.trustme.log.ui.model.FeelingState
import com.youllbecold.trustme.log.ui.model.LogState
import com.youllbecold.trustme.log.ui.model.WeatherParams

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
        clothes = clothes.mapNotNull { it.toClothesModel() }
    )

private fun FeelingState.toFeeling(): Feeling = when (this) {
    FeelingState.NORMAL -> Feeling.NORMAL
    FeelingState.COLD -> Feeling.COLD
    FeelingState.VERY_COLD -> Feeling.VERY_COLD
    FeelingState.WARM -> Feeling.WARM
    FeelingState.VERY_WARM -> Feeling.VERY_WARM
}

fun WeatherParams.toWeatherData(): WeatherData = WeatherData(
    apparentTemperatureMinC = apparentTemperatureMin,
    apparentTemperatureMaxC = apparentTemperatureMax,
    avgTemperatureC = avgTemperature,
)
