package com.youllbecold.logdatabase.internal.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.logdatabase.internal.data.dao.LogDao
import com.youllbecold.logdatabase.internal.data.mappers.toLogEntity
import com.youllbecold.logdatabase.internal.data.mappers.toLogData
import com.youllbecold.logdatabase.model.LogData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

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

    override suspend fun getLogsInRange(apparentTempRange: Pair<Double, Double>): List<LogData> =
        logDao.getAllInRange(
            apparentTempRange.first,
            apparentTempRange.second
        ).map { it.toLogData() }

    override suspend fun getLog(id: Int): LogData? =
        logDao.getById(id)
            .first()
            ?.toLogData()

    override suspend fun addLog(log: LogData) {
        logDao.insert(log.toLogEntity())
    }

    override suspend fun updateLog(log: LogData) {
        logDao.update(log.toLogEntity())
    }

    override suspend fun deleteLog(log: LogData) {
        logDao.delete(log.toLogEntity())
    }
}
