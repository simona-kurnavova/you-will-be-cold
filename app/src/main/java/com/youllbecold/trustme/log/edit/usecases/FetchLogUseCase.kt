package com.youllbecold.trustme.log.edit.usecases

import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.trustme.log.ui.model.LogState
import com.youllbecold.trustme.log.ui.model.mappers.toLogState

/**
 * Use case to fetch a log.
 */
class FetchLogUseCase(
    private val logResult: LogRepository
) {

    /**
     * Fetches a log.
     *
     * @param id The id of the log to fetch.
     */
    suspend fun fetchLog(id: Int): LogState? =
        logResult.getLog(id)?.toLogState()
}
