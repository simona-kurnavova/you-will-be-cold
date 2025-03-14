package com.youllbecold.trustme.log.history.usecases

import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.trustme.log.ui.model.LogState
import com.youllbecold.trustme.log.ui.mappers.toLogData

/**
 * Use case to delete a log from the database.
 */
class DeleteLogUseCase(
    private val logRepository: LogRepository
) {

    /**
     * Deletes a log from the database.
     */
    suspend fun deleteLog(log: LogState): Boolean =
        logRepository.deleteLog(log.toLogData())
}
