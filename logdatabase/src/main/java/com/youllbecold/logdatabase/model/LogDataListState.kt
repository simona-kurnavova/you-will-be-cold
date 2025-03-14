package com.youllbecold.logdatabase.model

/**
 * Return type for log state list requests.
 *
 * @param logs The list of logs.
 * @param isError Whether an error occurred.
 */
data class LogDataListState(
    val logs: List<LogData>,
    val isError: Boolean,
)