package com.youllbecold.trustme.log.edit.usecases

import android.annotation.SuppressLint
import android.app.Application
import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.trustme.common.data.permissions.PermissionChecker
import com.youllbecold.trustme.log.ui.model.LogState
import com.youllbecold.trustme.log.ui.model.mappers.toLogData
import com.youllbecold.trustme.common.ui.model.status.LoadingStatus
import com.youllbecold.trustme.log.usecases.ObtainLogWeatherParamsUseCase

/**
 * Use case to edit a log with weather data.
 */
class EditLogUseCase(
    private val app: Application,
    private val logRepository: LogRepository,
    private val obtainLogWeatherParamsUseCase: ObtainLogWeatherParamsUseCase,
) {
    /**
     * Updates a log with the weather data.
     * Note: Always considers only current location. This should be dealt with in the future
     *
     * @param oldLog The old log state.
     * @param newLog The new log state.
     */
    @SuppressLint("MissingPermission") // It is checked
    suspend fun updateLog(oldLog: LogState, newLog: LogState): LoadingStatus {
        when {
            oldLog == newLog -> return LoadingStatus.Success
            !PermissionChecker.hasLocationPermission(app) -> return LoadingStatus.MissingPermission
        }

        var log = newLog

        if (oldLog.date != newLog.date ||
            oldLog.timeFrom != newLog.timeFrom ||
            oldLog.timeTo != newLog.timeTo
        ) {
            val weather = obtainLogWeatherParamsUseCase.obtainWeather(newLog.dateTimeState)
                ?: return LoadingStatus.GenericError

            log = log.copy(weather = weather)
        }

        logRepository.updateLog(log.toLogData())
        return LoadingStatus.Success
    }
}