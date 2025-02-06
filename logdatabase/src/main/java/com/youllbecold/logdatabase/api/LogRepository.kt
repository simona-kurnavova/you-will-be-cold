package com.youllbecold.logdatabase.api

import com.youllbecold.logdatabase.model.Log
import kotlinx.coroutines.flow.Flow

interface LogRepository {
    /**
     * Returns all logs.
     */
    val logs: Flow<List<Log>>

    /**
     * Adds a new log.
     */
    suspend fun addLog(log: Log)

    /**
     * Updates a log.
     */
    suspend fun updateLog(log: Log)

    /**
     * Deletes a log.
     */
    suspend fun deleteLog(log: Log)
}