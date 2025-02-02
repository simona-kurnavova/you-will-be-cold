package com.youllbecold.trustme.weatherservice.internal.response

import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double,

    @SerializedName("current")
    val current: Current,

    @SerializedName("current_units")
    val units: Units,
)

data class Current(
    @SerializedName("time")
    val time: String,

    @SerializedName("temperature_2m")
    val temperature2m: Double,

    @SerializedName("apparent_temperature")
    val apparentTemperature: Double,

    @SerializedName("relative_humidity_2m")
    val relativeHumidity: Int,

    @SerializedName("wind_speed_10m")
    val windSpeed: Double,

    @SerializedName("weather_code")
    val weatherCode: Int,

    @SerializedName("precipitation_probability")
    val precipitationProbability: Int,

    @SerializedName("uv_index")
    val uvIndex: Double,
)
