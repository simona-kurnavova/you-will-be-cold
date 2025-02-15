package com.youllbecold.logdatabase.internal.log

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.youllbecold.logdatabase.internal.log.entity.LogEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface LogDao {
    @Query("SELECT * FROM log ORDER BY datetime(date_from) DESC LIMIT :limit")
    fun getAll(limit: Int = DEFAULT_LIMIT): Flow<List<LogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(logEntity: LogEntity)

    @Update
    fun update(logEntity: LogEntity)

    @Delete
    fun delete(logEntity: LogEntity)
}

private const val DEFAULT_LIMIT = 100
