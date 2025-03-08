package com.youllbecold.recomendation.internal.data.logs

import com.youllbecold.logdatabase.api.LogRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Responsibility of log collector is to collect relevant logs from the database.
 */
internal class LogCollector(
    private val logRepository: LogRepository
) {
    /**
     * Collects the logs closest to given range of apparent temperatures.
     *
     * @param minApparentTemp Minimum apparent temperature in Celsius.
     * @param maxApparentTemp Maximum apparent temperature in Celsius.
     *
     * @return List of relevant logs.
     */
    suspend fun gatherRelevantLogs(
        minApparentTemp: Double,
        maxApparentTemp: Double
    ) = withContext(Dispatchers.Default) {
        var expandConstant = 0.0

        while (expandConstant <= MAX_RANGE_EXPAND) {
            val logs = logRepository.getLogsInRange(
                minApparentTemp - expandConstant to maxApparentTemp + expandConstant
            )

            if (logs.isNotEmpty()) {
                return@withContext logs
            }
            expandConstant += 1.0
        }

        return@withContext emptyList()
    }
}

private const val MAX_RANGE_EXPAND = 5.0
