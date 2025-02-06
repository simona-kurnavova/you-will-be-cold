package com.youllbecold.logdatabase.model

import java.time.LocalDateTime

data class Log(
    val id: Int? = null,
    val date: LocalDateTime,
    val overallFeeling: Feeling,
    val weatherData: WeatherData
) {
    enum class Feeling {
        NORMAL,
        COLD,
        VERY_COLD,
        WARM,
        VERY_WARM
    }

    data class WeatherData(
        val temperature: Double,
        val apparentTemperature: Double
    )
}

