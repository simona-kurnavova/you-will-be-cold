package com.youllbecold.weather.internal.request

/**
 * Enum class that represents the different temperature units that can be requested.
 */
internal enum class TemperatureUnitRequest(val value: String) {
    CELSIUS("celsius"),
    FAHRENHEIT("fahrenheit"),
}
