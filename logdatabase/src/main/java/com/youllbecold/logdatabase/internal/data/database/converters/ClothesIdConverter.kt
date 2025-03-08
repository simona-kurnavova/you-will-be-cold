package com.youllbecold.logdatabase.internal.data.database.converters

import androidx.room.TypeConverter
import com.youllbecold.logdatabase.internal.data.model.ClothesId

internal class ClothesIdConverter {
    @TypeConverter
    fun fromClothesId(clothesId: ClothesId?): String? = clothesId?.name

    @TypeConverter
    fun toClothesId(value: String?): ClothesId? = value?.let { enumValueOf<ClothesId>(it) }
}

internal class ClothesIdListConverter {
    @TypeConverter
    fun fromClothesIdList(clothesIds: List<ClothesId>?): String? {
        return clothesIds?.joinToString(separator = ",") { it.name }
    }

    @TypeConverter
    fun toClothesIdList(value: String?): List<ClothesId>? {
        return if (value.isNullOrBlank()) {
            emptyList()
        } else {
            value.split(",")
                .map { enumValueOf<ClothesId>(it) }
        }
    }
}
