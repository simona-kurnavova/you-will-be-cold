package com.youllbecold.trustme.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.youllbecold.trustme.database.converters.DateConverter
import com.youllbecold.trustme.database.converters.FeelingConverter
import com.youllbecold.trustme.database.converters.WeatherDataConverter
import com.youllbecold.trustme.database.entity.LogEntity

/**
 * Database for logs.
 */
@Database(
    entities = [LogEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(DateConverter::class, FeelingConverter::class, WeatherDataConverter::class)
abstract class LogDatabase : RoomDatabase() {

    abstract fun logDao(): LogDao

    companion object {
        private const val DATABASE_NAME = "log-database"

        /**
         * Build the database.
         * We will rely on DI to call this only once and make it singleton.
         */
        fun buildDatabase(app: Application): LogDatabase =
            Room.databaseBuilder(
                app,
                LogDatabase::class.java,
                DATABASE_NAME
            ).build()
    }
}