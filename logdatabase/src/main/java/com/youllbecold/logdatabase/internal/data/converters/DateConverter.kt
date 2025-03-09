package com.youllbecold.logdatabase.internal.data.converters

import androidx.room.TypeConverter
import java.time.LocalDateTime

internal class DateConverter {
    @TypeConverter
    internal fun fromTimestamp(value: String): LocalDateTime? =
       LocalDateTime.parse(value)

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }
}