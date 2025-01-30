package com.youllbecold.trustme.weatherservice

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val elevation: Double,
    @SerializedName("generationtime_ms")
    val generationtimeMs: Double,
    @SerializedName("utc_offset_seconds")
    val utcOffsetSeconds: Int,
    val timezone: String,
    @SerializedName("timezone_abbreviation")
    val timezoneAbbreviation: String,
    val hourly: Hourly,
    @SerializedName("hourly_units")
    val hourlyUnits: HourlyUnits
)

data class Hourly(
    val time: List<String>,
    @SerializedName("temperature_2m")
    val temperature2m: List<Double>
)

data class HourlyUnits(
    @SerializedName("temperature_2m")
    val temperature2m: String
)
