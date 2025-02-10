package com.youllbecold.logdatabase.internal.log.converters

import androidx.room.TypeConverter
import com.youllbecold.logdatabase.internal.log.entity.FeelingEntity

internal class FeelingEntityConverter {
    @TypeConverter
    fun fromFeelingEntity(feeling: FeelingEntity?): String? = feeling?.name

    @TypeConverter
    fun toFeelingEntity(value: String?): FeelingEntity? = value?.let { enumValueOf<FeelingEntity>(it) }
}
