package com.youllbecold.logdatabase.api

import com.youllbecold.logdatabase.model.LogData
import kotlinx.coroutines.flow.Flow

/**
 * A repository for logs.
 */
interface LogRepository {
    /**
     * Returns all logs.
     */
    val logs: Flow<List<LogData>>

    /**
     * Returns logs within the given apparent temperature range.
     *
     * @param apparentTempRange The range of apparent temperatures to filter logs by. Only inside this range.
     */
    suspend fun getLogsInRange(apparentTempRange: Pair<Double, Double>): List<LogData>

    /**
     * Returns a log by its ID.
     */
    suspend fun getLog(id: Int): LogData?

    /**
     * Adds a new log.
     */
    suspend fun addLog(log: LogData)

    /**
     * Updates a log.
     */
    suspend fun updateLog(log: LogData)

    /**
     * Deletes a log.
     */
    suspend fun deleteLog(log: LogData)
}
