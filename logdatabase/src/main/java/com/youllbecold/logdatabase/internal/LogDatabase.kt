package com.youllbecold.logdatabase.internal

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.youllbecold.logdatabase.internal.converters.DateConverter
import com.youllbecold.logdatabase.internal.entity.LogEntity

/**
 * Database for logs.
 */
@Database(
    entities = [LogEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
internal abstract class LogDatabase : RoomDatabase() {

    abstract fun logDao(): LogDao

    companion object {
        private const val DATABASE_NAME = "log-database"

        private var INSTANCE: LogDatabase? = null

        /**
         * Get the instance of the database (singleton).
         */
        fun getInstance(application: Application): LogDatabase {
            return INSTANCE ?: synchronized(this) {
                buildDatabase(application)
            }.also { INSTANCE = it }
        }

        /**
         * Build the database.
         * We will rely on DI to call this only once and make it singleton.
         */
        private fun buildDatabase(app: Application): LogDatabase =
            Room.databaseBuilder(
                app,
                LogDatabase::class.java,
                DATABASE_NAME
            ).build()
    }
}