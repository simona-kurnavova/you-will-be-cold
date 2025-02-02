package com.youllbecold.trustme.weatherservice.internal.response

import com.google.gson.annotations.SerializedName

/**
 * Units for weather data.
 */
data class Units(
    @SerializedName("temperature_2m")
    val temperatureUnit: TemperatureUnit
)

enum class TemperatureUnit {
    @SerializedName("°C")
    CELSIUS,
    @SerializedName("°F")
    FAHRENHEIT,
}
