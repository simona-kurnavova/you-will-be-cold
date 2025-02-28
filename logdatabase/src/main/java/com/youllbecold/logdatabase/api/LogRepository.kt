package com.youllbecold.logdatabase.api

import androidx.paging.PagingData
import com.youllbecold.logdatabase.model.LogData
import kotlinx.coroutines.flow.Flow

/**
 * A repository for logs.
 */
interface LogRepository {
    /**
     * Returns all logs with paging.
     *
     * @param pageSize The size of each page.
     */
    fun getAllWithPaging(pageSize: Int = PAGER_PAGE_SIZE): Flow<PagingData<LogData>>

    /**
     * Returns logs within the given apparent temperature range.
     *
     * @param apparentTempRange The range of apparent temperatures to filter logs by. Only inside this range.
     */
    suspend fun getLogsInRange(apparentTempRange: Pair<Double, Double>): List<LogData>

    /**
     * Returns a log by its ID.
     *
     * @param id The ID of the log to return.
     */
    suspend fun getLog(id: Int): LogData?

    /**
     * Adds a new log.
     *
     * @param log The log to add.
     */
    suspend fun addLog(log: LogData)

    /**
     * Updates a log.
     *
     * @param log The log to update.
     */
    suspend fun updateLog(log: LogData)

    /**
     * Deletes a log.
     *
     * @param log The log to delete.
     */
    suspend fun deleteLog(log: LogData)
}

private const val PAGER_PAGE_SIZE: Int = 20
