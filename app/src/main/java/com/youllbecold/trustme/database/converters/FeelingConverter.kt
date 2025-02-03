package com.youllbecold.trustme.database.converters

import androidx.room.TypeConverter
import com.youllbecold.trustme.database.entity.Feeling

class FeelingConverter {
    @TypeConverter
    fun fromFeeling(value: Feeling?): String? {
        return value?.name
    }

    @TypeConverter
    fun toFeeling(value: String?): Feeling? {
        return value?.let { Feeling.valueOf(it) }
    }
}