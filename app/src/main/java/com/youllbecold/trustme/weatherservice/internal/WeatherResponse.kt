package com.youllbecold.trustme.weatherservice.internal

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double,

    @SerializedName("elevation")
    val elevation: Double,

    @SerializedName("generationtime_ms")
    val generationtimeMs: Double,

    @SerializedName("utc_offset_seconds")
    val utcOffsetSeconds: Int,

    @SerializedName("timezone")
    val timezone: String,

    @SerializedName("timezone_abbreviation")
    val timezoneAbbreviation: String,

    @SerializedName("forecast_days")
    val hourly: Hourly?,

    @SerializedName("hourly_units")
    val hourlyUnits: HourlyUnits
)

data class Hourly(
    @SerializedName("time")
    val time: List<String>,

    @SerializedName("temperature_2m")
    val temperature2m: List<Double>
)

data class HourlyUnits(
    @SerializedName("temperature_2m")
    val temperature2m: String
)
