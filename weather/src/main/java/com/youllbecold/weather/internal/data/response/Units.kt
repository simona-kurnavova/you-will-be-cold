package com.youllbecold.weather.internal.data.response

import com.google.gson.annotations.SerializedName

/**
 * Units for weather data.
 */
internal data class Units(
    @SerializedName("temperature_2m")
    val temperatureUnit: TemperatureUnit
)

internal enum class TemperatureUnit {
    @SerializedName("°C")
    CELSIUS,
    @SerializedName("°F")
    FAHRENHEIT,
}
