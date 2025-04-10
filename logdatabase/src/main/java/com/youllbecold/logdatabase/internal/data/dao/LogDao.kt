package com.youllbecold.logdatabase.internal.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.youllbecold.logdatabase.internal.data.model.LogEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface LogDao {
    @Query("SELECT * FROM log ORDER BY datetime(date_from) DESC")
    fun getAllPaging(): PagingSource<Int, LogEntity>

    @Query("SELECT * FROM log WHERE " +
            "apparentTemperatureMinC >= :apparentTempLower AND " +
            "apparentTemperatureMaxC <= :apparentTempUpper " +
            "ORDER BY datetime(date_from) DESC " +
            "LIMIT :limit")
    suspend fun getAllInRange(
        apparentTempLower: Double,
        apparentTempUpper: Double,
        limit: Int = DEFAULT_LIMIT
    ): List<LogEntity>

    @Query("SELECT * FROM log WHERE id = :id LIMIT 1")
    fun getById(id: Int): Flow<LogEntity?>

    @Upsert
    fun insert(logEntity: LogEntity): Long

    @Update
    fun update(logEntity: LogEntity): Int

    @Delete
    fun delete(logEntity: LogEntity): Int
}

private const val DEFAULT_LIMIT = 1000
