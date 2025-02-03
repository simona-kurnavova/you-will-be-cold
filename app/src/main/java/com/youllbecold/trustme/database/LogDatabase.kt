package com.youllbecold.trustme.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.youllbecold.trustme.database.converters.DateConverter
import com.youllbecold.trustme.database.converters.FeelingConverter
import com.youllbecold.trustme.database.converters.WeatherDataConverter
import com.youllbecold.trustme.database.entity.LogEntity

@Database(
    entities = [LogEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(DateConverter::class, FeelingConverter::class, WeatherDataConverter::class)
abstract class LogDatabase : RoomDatabase() {

    abstract fun logDao(): LogDao

    companion object {
        const val DATABASE_NAME = "log-database"
    }
}