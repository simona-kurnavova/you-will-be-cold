package com.youllbecold.trustme.log.history.usecases

import androidx.paging.PagingData
import androidx.paging.map
import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.trustme.log.ui.model.LogState
import com.youllbecold.trustme.log.ui.model.mappers.toLogState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Use case to fetch all logs from the database.
 */
class FetchAllLogsUseCase(
    private val logRepository: LogRepository,
) {
    /**
     * Fetches all logs from the database.
     *
     * @param useCelsiusUnits Whether to use Celsius units.
     */
    fun fetchAllLogs(useCelsiusUnits: Boolean): Flow<PagingData<LogState>> = logRepository
        .getAllWithPaging()
        .map { pagingData ->
            pagingData.map { it.toLogState(useCelsiusUnits) }
        }
}
