package com.youllbecold.logdatabase.internal.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.logdatabase.internal.data.dao.LogDao
import com.youllbecold.logdatabase.internal.data.mappers.toLogEntity
import com.youllbecold.logdatabase.internal.data.mappers.toLogData
import com.youllbecold.logdatabase.model.LogData
import com.youllbecold.logdatabase.model.LogDataListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Implementation of [LogRepository].
 */
internal class LogRepositoryImpl(
    private val logDao: LogDao
) : LogRepository {
    override fun getAllWithPaging(pageSize: Int): Flow<PagingData<LogData>> = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { logDao.getAllPaging() }
    ).flow.map { pagingData ->
        pagingData.map { logEntity -> logEntity.toLogData() }
    }

    override suspend fun getLogsInRange(apparentTempRange: Pair<Double, Double>): LogDataListState {
        val (success, logs) = runQuerySafely {
            logDao.getAllInRange(
                apparentTempRange.first,
                apparentTempRange.second
            ).map { it.toLogData() }
        }

        return LogDataListState(
            logs = logs ?: emptyList(),
            isError = !success
        )
    }

    override suspend fun getLog(id: Int): LogData? =
        runQuerySafely {
            logDao.getById(id)
                .first()
                ?.toLogData()
        }.second


    override suspend fun addLog(log: LogData): Boolean =
        runQuerySafely {
            val result = logDao.insert(log.toLogEntity())
            result > 0
        }.first

    override suspend fun updateLog(log: LogData): Boolean =
        runQuerySafely {
            val result = logDao.update(log.toLogEntity())
            result > 0
        }.first

    override suspend fun deleteLog(log: LogData): Boolean =
        runQuerySafely {
            val result = logDao.delete(log.toLogEntity())
            result > 0
        }.first

    private suspend fun <T> runQuerySafely(block: suspend () -> T?): Pair<Boolean, T?> =
        withContext(Dispatchers.IO) {
            try {
                val result = block()
                return@withContext true to result
            } catch (e: Exception) {
                Log.d("LogRepositoryImpl", "Error while executing query: ${e.message}")
                return@withContext false to null
            }
        }
}
