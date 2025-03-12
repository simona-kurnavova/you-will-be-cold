package com.youllbecold.logdatabase.model

import java.time.LocalDateTime

data class LogData(
    val id: Int? = null,
    val dateFrom: LocalDateTime,
    val dateTo: LocalDateTime,
    val weatherData: WeatherData,
    val feelings: Feelings,
    val clothes: List<ClothesModel>
)

data class WeatherData(
    val apparentTemperatureMinC: Double,
    val apparentTemperatureMaxC: Double,
    val avgTemperatureC: Double
)

data class Feelings(
    val head: Feeling,
    val neck: Feeling,
    val top: Feeling,
    val bottom: Feeling,
    val feet: Feeling,
    val hand: Feeling
)

enum class Feeling {
    VERY_COLD,
    COLD,
    NORMAL,
    WARM,
    VERY_WARM
}
