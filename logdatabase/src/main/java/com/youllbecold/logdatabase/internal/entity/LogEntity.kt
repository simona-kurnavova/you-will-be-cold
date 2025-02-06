package com.youllbecold.logdatabase.internal.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

@Entity(tableName = "log")
internal data class LogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "date")
    val date: LocalDateTime,

    //@ColumnInfo(name = "overall_feeling")
    //val overallFeeling: FeelingEntity,

    @SerializedName("temperature")
    val temperature: Double,

    @SerializedName("apparent_temperature")
    val apparentTemperature: Double,
)

enum class FeelingEntity(val value: Int) {
    VERY_COLD(0),
    COLD(1),
    NORMAL(2),
    WARM(3),
    VERY_WARM(4)
}