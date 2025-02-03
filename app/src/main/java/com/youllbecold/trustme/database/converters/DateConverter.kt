package com.youllbecold.trustme.database.converters

import androidx.room.TypeConverter
import java.time.LocalDateTime

class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: String): LocalDateTime? =
       LocalDateTime.parse(value)

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }
}