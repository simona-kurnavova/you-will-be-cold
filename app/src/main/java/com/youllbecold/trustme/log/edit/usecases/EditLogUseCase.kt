package com.youllbecold.trustme.log.edit.usecases

import android.annotation.SuppressLint
import android.app.Application
import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.trustme.common.data.permissions.PermissionChecker
import com.youllbecold.trustme.common.ui.model.status.Error
import com.youllbecold.trustme.log.ui.model.LogState
import com.youllbecold.trustme.log.ui.mappers.toLogData
import com.youllbecold.trustme.common.ui.model.status.Status
import com.youllbecold.trustme.common.ui.model.status.Success
import com.youllbecold.trustme.log.domain.LogWeatherProvider

/**
 * Use case to edit a log with weather data.
 */
class EditLogUseCase(
    private val app: Application,
    private val logRepository: LogRepository,
    private val logWeatherProvider: LogWeatherProvider,
) {
    /**
     * Updates a log with the weather data.
     * Note: Always considers only current location. This should be dealt with in the future
     *
     * @param oldLog The old log state.
     * @param newLog The new log state.
     */
    @SuppressLint("MissingPermission") // It is checked
    suspend fun updateLog(oldLog: LogState, newLog: LogState): Status {
        if (oldLog == newLog) {
            return Success // Nothing to update
        }

        var log = newLog

        if (oldLog.date != newLog.date ||
            oldLog.timeFrom != newLog.timeFrom ||
            oldLog.timeTo != newLog.timeTo
        ) {
            val weather = logWeatherProvider.obtainWeather(newLog.dateTimeState)

            when {
                weather.status != Success -> return weather.status
                weather.weatherParams == null -> return Error.ApiError
            }

            log = log.copy(weather = weather.weatherParams)
        }

        val successful = logRepository.updateLog(log.toLogData())
        return if (successful) Success else Error.ApiError
    }
}