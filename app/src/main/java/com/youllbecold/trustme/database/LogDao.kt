package com.youllbecold.trustme.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.youllbecold.trustme.database.entity.LogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LogDao {
    @Query("SELECT * FROM log LIMIT :limit")
    fun getAll(limit: Int = DEFAULT_LIMIT): Flow<List<LogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(logEntity: LogEntity)

    @Update
    fun update(logEntity: LogEntity)

    @Delete
    fun delete(logEntity: LogEntity)
}

private val DEFAULT_LIMIT = 100
