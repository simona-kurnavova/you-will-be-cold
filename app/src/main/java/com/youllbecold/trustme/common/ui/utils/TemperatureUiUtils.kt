package com.youllbecold.trustme.common.ui.utils

import android.content.Context
import com.youllbecold.trustme.R

/**
 * Utility class for temperature related operations.
 */
object TemperatureUiUtils {

    /**
     * Get the temperature string based on the units.
     */
    fun getTemperatureString(context: Context, temperature: Double, unitsCelsius: Boolean): String =
        if (unitsCelsius) {
            context.getString(R.string.temperature_celsius, temperature)
        } else {
            context.getString(R.string.temperature_fahrenheit, temperature)
        }

    /**
     * Convert a temperature from Celsius to Fahrenheit.
     *
     * @param fahrenheit The temperature in Fahrenheit.
     */
    fun fahrenheitToCelsius(fahrenheit: Double): Double =
        (fahrenheit - 32) * 5 / 9

    /**
     * Convert a temperature from Fahrenheit to Celsius.
     *
     * @param celsius The temperature in celsius.
     */
    fun celsiusToFahrenheit(celsius: Double): Double =
        (celsius * 9 / 5) + 32
}
