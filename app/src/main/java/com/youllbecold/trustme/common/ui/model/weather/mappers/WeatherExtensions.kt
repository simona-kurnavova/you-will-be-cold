package com.youllbecold.trustme.common.ui.model.weather.mappers

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.youllbecold.recomendation.model.RainRecommendation
import com.youllbecold.recomendation.model.UvRecommendation
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.components.themed.IconType
import com.youllbecold.trustme.common.ui.model.weatherwithrecommend.WeatherWithRecommendation
import com.youllbecold.weather.model.Weather
import com.youllbecold.weather.model.WeatherEvaluation

/**
 * Get icon for [WeatherEvaluation].
 */
val WeatherEvaluation.icon: IconType
    get() = when (this) {
        WeatherEvaluation.SUNNY -> IconType.Sun
        WeatherEvaluation.CLOUDY -> IconType.Cloud
        WeatherEvaluation.RAINY -> IconType.Rain
        WeatherEvaluation.SNOWY -> IconType.Snowflake
        WeatherEvaluation.STORM -> IconType.Lightning
        WeatherEvaluation.FOGGY -> IconType.Fog
        WeatherEvaluation.UNKNOWN -> IconType.Cloud
    }

/**
 * Get the thermometer image based on the Weather temperature.
 */
@get:DrawableRes
val Weather.thermometer: Int
    get() {
        val celsius = if (unitsCelsius) temperature else fahrenheitToCelsius(temperature)
        return when {
            celsius < 0 -> R.drawable.thermometer_0
            celsius < 10 -> R.drawable.thermometer_1
            celsius < 20 -> R.drawable.thermometer_2
            else -> R.drawable.thermometer_3
        }
    }

private fun fahrenheitToCelsius(fahrenheit: Double): Double =
    (fahrenheit - 32) * 5 / 9

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
 * Get the title for the [UvRecommendation].
 */
@Composable
fun UvRecommendation.getTitle(): String = when (this) {
    UvRecommendation.NoProtection -> stringResource(R.string.uv_recom_none)
    UvRecommendation.LowProtection -> stringResource(R.string.uv_recom_low)
    UvRecommendation.MediumProtection -> stringResource(R.string.uv_recom_medium)
    UvRecommendation.HighProtection -> stringResource(R.string.uv_recom_high)
}

/**
 * Get the title for the [RainRecommendation].
 */
@Composable
fun RainRecommendation.getTitle(): String = when (this) {
    RainRecommendation.NoRain -> stringResource(R.string.rain_recom_none)
    RainRecommendation.LightRain -> stringResource(R.string.rain_recom_low)
    RainRecommendation.MediumRain -> stringResource(R.string.rain_recom_medium)
    RainRecommendation.HeavyRain -> stringResource(R.string.rain_recom_high)
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
    valueSelector: (Weather) -> Double,
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
