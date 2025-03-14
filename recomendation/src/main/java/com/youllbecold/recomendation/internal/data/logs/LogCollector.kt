package com.youllbecold.recomendation.internal.data.logs

import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.logdatabase.model.LogData
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
    ): RelevantLogs = withContext(Dispatchers.Default) {
        var expandConstant = 0.0

        while (expandConstant <= MAX_RANGE_EXPAND) {
            val logs = logRepository.getLogsInRange(
                minApparentTemp - expandConstant to maxApparentTemp + expandConstant
            )

            when {
                logs.isError -> return@withContext RelevantLogs(result = LogQueryResult.Error)
                logs.logs.isNotEmpty() ->
                    return@withContext RelevantLogs(result = LogQueryResult.Success, logs = logs.logs)
            }
            expandConstant += 1.0
        }

        return@withContext RelevantLogs(result = LogQueryResult.NotEnoughLogs)
    }
}

private const val MAX_RANGE_EXPAND = 5.0

data class RelevantLogs(
    val result: LogQueryResult,
    val logs: List<LogData> = emptyList(),
)

enum class LogQueryResult {
    Success,
    NotEnoughLogs,
    Error
}
