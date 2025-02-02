package com.youllbecold.trustme.weatherservice.model

data class WeatherNow(
    val city: String?,
    val unitsCelsius: Boolean,
    val temperature: Double,
    val apparentTemperature: Double,
    val weatherEvaluation: WeatherEvaluation,
    val relativeHumidity: Int,
    val windSpeed: Double,
    val precipitationProbability: Int,
    val uvIndex: Double,
)
