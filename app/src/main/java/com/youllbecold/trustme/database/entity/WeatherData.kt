package com.youllbecold.trustme.database.entity

import com.google.gson.annotations.SerializedName

data class WeatherData(
    @SerializedName("temperature")
    val temperature: Double,

    @SerializedName("apparent_temperature")
    val apparentTemperature: Double,
)
