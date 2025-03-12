package com.youllbecold.trustme.recommend.ui.mappers

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.youllbecold.trustme.R
import com.youllbecold.trustme.recommend.ui.model.WeatherConditions
import com.youllbecold.trustme.recommend.ui.model.WeatherWithRecommendation

/**
 * Get the temperature string based on the units.
 */
fun Context.getTemperatureString(temperature: Double, unitsCelsius: Boolean): String =
    if (unitsCelsius) {
        getString(R.string.temperature_celsius, temperature)
    } else {
        getString(R.string.temperature_fahrenheit, temperature)
    }

/**
 * Get description for the temperature range.
 */
@Composable
fun WeatherWithRecommendation.temperatureRangeDescription(): String {
    return description(
        valueSelector = { it.temperature },
        singleResId = R.string.recom_description_single,
        rangeResId = R.string.recom_description_range
    )
}

/**
 * Get description for the apparent temperature range.
 */
@Composable
fun WeatherWithRecommendation.feelLikeDescription(): String {
    return description(
        valueSelector = { it.apparentTemperature },
        singleResId = R.string.recom_apparent_description_single,
        rangeResId = R.string.recom_apparent_description
    )
}

@Composable
private fun WeatherWithRecommendation.description(
    valueSelector: (WeatherConditions) -> Double,
    singleResId: Int,
    rangeResId: Int
): String {
    val context = LocalContext.current
    val usesCelsius = weather.first().unitsCelsius

    val min = context.getTemperatureString(weather.minOf(valueSelector), usesCelsius)
    val max = context.getTemperatureString(weather.maxOf(valueSelector), usesCelsius)

    return if (weather.size == 1) {
        stringResource(singleResId, min)
    } else {
        stringResource(rangeResId, min, max)
    }
}
