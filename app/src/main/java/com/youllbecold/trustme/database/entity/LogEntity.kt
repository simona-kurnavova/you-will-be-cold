package com.youllbecold.trustme.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "log")
data class LogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,

    @ColumnInfo(name = "date")
    val date: LocalDateTime,

    @ColumnInfo(name = "overall_feeling")
    val overallFeeling: Feeling,

    @ColumnInfo(name = "weather_data")
    val weatherData: WeatherData
)
