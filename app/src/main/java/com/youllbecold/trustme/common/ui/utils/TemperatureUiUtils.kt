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
}
