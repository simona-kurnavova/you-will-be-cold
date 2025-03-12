package com.youllbecold.trustme.recommend.ui.mappers

import com.youllbecold.trustme.common.ui.components.themed.IconType
import com.youllbecold.trustme.recommend.ui.model.WeatherConditions
import com.youllbecold.weather.model.WeatherModel
import com.youllbecold.weather.model.WeatherEvaluation
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Map [WeatherModel] to [WeatherConditions] to be used in UI.
 */
fun WeatherModel.toWeatherConditions(): WeatherConditions =
    WeatherConditions(
        time = time,
        unitsCelsius = unitsCelsius,
        temperature = temperature,
        apparentTemperature = apparentTemperature,
        icon = weatherEvaluation.icon,
        relativeHumidity = relativeHumidity,
        windSpeed = windSpeed,
        precipitationProbability = precipitationProbability,
        uvIndex = uvIndex
    )

/**
 * Map [List] of [WeatherModel] to [PersistentList] of [WeatherConditions] to be used in UI.
 */
suspend fun List<WeatherModel>.toWeatherCondPersistList(): PersistentList<WeatherConditions> =
    withContext(Dispatchers.Default) {
        map {
            it.toWeatherConditions()
        }.toPersistentList()
    }

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
        WeatherEvaluation.UNKNOWN -> IconType.Cloud // Why? Because I said so. It is cloudy now.
    }
