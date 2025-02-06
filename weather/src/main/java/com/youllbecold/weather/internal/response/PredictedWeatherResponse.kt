package com.youllbecold.weather.internal.response

import com.google.gson.annotations.SerializedName

internal data class PredictedWeatherResponse(
    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double,

    @SerializedName("hourly")
    val hourly: Hourly,

    @SerializedName("hourly_units")
    val hourlyUnits: Units
)

internal data class Hourly(
    @SerializedName("time")
    val time: List<String>,

    @SerializedName("temperature_2m")
    val temperature2m: List<Double>,

    @SerializedName("apparent_temperature")
    val apparentTemperature: List<Double>,

    @SerializedName("relative_humidity_2m")
    val relativeHumidity: List<Int>,

    @SerializedName("wind_speed_10m")
    val windSpeed: List<Double>,

    @SerializedName("weather_code")
    val weatherCode: List<Int>,

    @SerializedName("precipitation_probability")
    val precipitationProbability: List<Int>,

    @SerializedName("uv_index")
    val uvIndex: List<Double>,
)
