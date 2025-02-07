package com.youllbecold.weather.model

import java.time.LocalDateTime

data class Weather(
    val time: LocalDateTime,
    val unitsCelsius: Boolean,
    val temperature: Double,
    val apparentTemperature: Double,
    val weatherEvaluation: WeatherEvaluation,
    val relativeHumidity: Int,
    val windSpeed: Double,
    val precipitationProbability: Int,
    val uvIndex: Double,
)
