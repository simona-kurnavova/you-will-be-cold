package com.youllbecold.trustme.model

import java.util.Date
data class Weather(
    val city: String,
    val elevation: Float,
    val date: Date,
    val timezone: String,
    val timezoneAbbreviation: String,
    val temperatureUnit: TemperatureUnit,
    val currentTemperature: Float,
    val hourlyTemperatures: List<Temperature>,
    val evaluation: Evaluation
)

enum class TemperatureUnit {
    CELSIUS,
    FAHRENHEIT
}

data class Temperature(
    val time: String,
    val temperature: Double
)

enum class Evaluation {
    SUNNY,
    CLOUDY,
    SUNNY_CLOUDY,
    RAINY,
    SNOWY,
    WINDY,
    NIGHT,
    STORM,
    // FOGGY,
}
