package com.youllbecold.trustme.log.add.usecases

import android.annotation.SuppressLint
import com.youllbecold.logdatabase.api.LogRepository
import com.youllbecold.trustme.common.ui.model.status.Error
import com.youllbecold.trustme.log.ui.model.LogState
import com.youllbecold.trustme.log.ui.mappers.toLogData
import com.youllbecold.trustme.common.ui.model.status.Status
import com.youllbecold.trustme.common.ui.model.status.Success
import com.youllbecold.trustme.common.ui.model.status.isSuccess
import com.youllbecold.trustme.log.domain.LogWeatherProvider

/**
 * Use case to add a log with weather data.
 */
class AddLogUseCase(
    private val logRepository: LogRepository,
    private val logWeatherProvider: LogWeatherProvider,
) {

    /**
     * Saves a log with the weather data.
     */
    @SuppressLint("MissingPermission") // It is checked
    suspend fun saveLogWithWeather(logState: LogState): Status {
        val weather = logWeatherProvider.obtainWeather(logState.dateTimeState)

        when {
            !weather.status.isSuccess() -> return weather.status
            weather.weatherParams == null -> return Error.ApiError
        }

        logRepository.addLog(
            logState.copy(weather = weather.weatherParams).toLogData()
        )

        return Success
    }
}
