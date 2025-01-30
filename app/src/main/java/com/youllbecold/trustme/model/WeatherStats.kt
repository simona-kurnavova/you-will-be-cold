package com.youllbecold.trustme.model

data class WeatherStats(
    val latitude: Double,
    val longitude: Double,
    val elevation: Double,
    val timezone: String,
    val timezoneAbbreviation: String,
    val temperatures: Temperatures
)

data class Temperatures(
    val time: List<String>,
    val temperatures: List<Double>
)
