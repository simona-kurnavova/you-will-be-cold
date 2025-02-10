package com.youllbecold.logdatabase.internal.log.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

@Entity(tableName = "log")
internal data class LogEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "date_from")
    val dateFrom: LocalDateTime,

    @ColumnInfo(name = "date_to")
    val dateTo: LocalDateTime,

    @SerializedName("apparent_temperature_min")
    val apparentTemperatureMin: Double,

    @SerializedName("apparent_temperature_max")
    val apparentTemperatureMax: Double,

    @SerializedName("avg_temperature")
    val avgTemperature: Double,

    @ColumnInfo(name = "overall_feeling")
    val overallFeeling: FeelingEntity,

    @ColumnInfo(name = "clothes")
    val clothes: List<ClothesId>
)
