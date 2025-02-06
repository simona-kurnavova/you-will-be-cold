package com.youllbecold.weather.model

import java.time.LocalDateTime

data class WeatherPrediction(
    val unitsCelsius: Boolean,
    val hourlyData: List<HourlyData>,
)

data class HourlyData(
    val time: LocalDateTime,
    val temperature: Double,
    val apparentTemperature: Double,
    val weatherEvaluation: WeatherEvaluation,
    val relativeHumidity: Int,
    val windSpeed: Double,
    val precipitationProbability: Int,
    val uvIndex: Double,
)

