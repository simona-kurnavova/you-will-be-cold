package com.youllbecold.trustme.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.youllbecold.trustme.database.entity.WeatherData

class WeatherDataConverter {

    @TypeConverter
    fun fromWeatherData(weatherData: WeatherData?): String? {
        return weatherData?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toWeatherData(weatherDataJson: String?): WeatherData? {
        return weatherDataJson?.let { Gson().fromJson(it, WeatherData::class.java) }
    }
}
