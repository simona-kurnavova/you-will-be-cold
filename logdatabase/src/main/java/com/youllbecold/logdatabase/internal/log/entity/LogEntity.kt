package com.youllbecold.logdatabase.internal.log.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

@Entity(
    tableName = "log",
    indices = [
        Index(value = ["apparentTemperatureMinC"]),
        Index(value = ["apparentTemperatureMaxC"])
    ]
)
internal data class LogEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "date_from")
    val dateFrom: LocalDateTime,

    @ColumnInfo(name = "date_to")
    val dateTo: LocalDateTime,

    @SerializedName("apparent_temperature_min")
    val apparentTemperatureMinC: Double,

    @SerializedName("apparent_temperature_max")
    val apparentTemperatureMaxC: Double,

    @SerializedName("avg_temperature")
    val avgTemperatureC: Double,

    @ColumnInfo(name = "head_feeling")
    val headFeeling: FeelingEntity,

    @ColumnInfo(name = "neck_feeling")
    val neckFeeling: FeelingEntity,

    @ColumnInfo(name = "top_feeling")
    val topFeeling: FeelingEntity,

    @ColumnInfo(name = "bottom_feeling")
    val bottomFeeling: FeelingEntity,

    @ColumnInfo(name = "feet_feeling")
    val feetFeeling: FeelingEntity,

    @ColumnInfo(name = "hand_feeling")
    val handFeeling: FeelingEntity,

    @ColumnInfo(name = "clothes")
    val clothes: List<ClothesId>
)
