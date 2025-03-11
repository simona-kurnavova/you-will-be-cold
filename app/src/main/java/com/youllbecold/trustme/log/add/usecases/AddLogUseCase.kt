package com.youllbecold.trustme.log.add.usecases

import android.annotation.SuppressLint
import android.app.Application
import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.trustme.common.data.permissions.PermissionChecker
import com.youllbecold.trustme.log.ui.model.LogState
import com.youllbecold.trustme.log.ui.model.mappers.toLogData
import com.youllbecold.trustme.common.ui.model.status.LoadingStatus
import com.youllbecold.trustme.log.usecases.ObtainLogWeatherParamsUseCase

/**
 * Use case to add a log with weather data.
 */
class AddLogUseCase(
    private val app: Application,
    private val logRepository: LogRepository,
    private val obtainLogWeatherParamsUseCase: ObtainLogWeatherParamsUseCase,
) {

    /**
     * Saves a log with the weather data.
     */
    @SuppressLint("MissingPermission") // It is checked
    suspend fun saveLogWithWeather(logState: LogState): LoadingStatus {
        if (!PermissionChecker.hasLocationPermission(app)) {
            return LoadingStatus.MissingPermission
        }

        val weather = obtainLogWeatherParamsUseCase.obtainWeather(logState.dateTimeState)

        if(weather == null) {
            return LoadingStatus.GenericError
        }

        logRepository.addLog(
            logState.copy(weather = weather).toLogData()
        )

        return LoadingStatus.Success
    }
}
