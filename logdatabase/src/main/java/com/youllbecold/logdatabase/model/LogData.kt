package com.youllbecold.logdatabase.model

import java.time.LocalDateTime

data class LogData(
    val id: Int? = null,
    val dateFrom: LocalDateTime,
    val dateTo: LocalDateTime,
    val weatherData: WeatherData,
    val overallFeeling: Feeling,
    val clothes: List<Clothes>
)

enum class Feeling {
    NORMAL,
    COLD,
    VERY_COLD,
    WARM,
    VERY_WARM
}

data class WeatherData(
    val apparentTemperatureMin: Double,
    val apparentTemperatureMax: Double,
    val avgTemperature: Double
)