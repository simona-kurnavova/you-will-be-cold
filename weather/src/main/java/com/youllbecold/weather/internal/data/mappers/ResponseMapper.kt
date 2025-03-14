package com.youllbecold.weather.internal.data.mappers

import com.youllbecold.weather.api.WeatherResult
import com.youllbecold.weather.api.WeatherResult.Success
import retrofit2.Response

/**
 * Maps a response to a weather result.
 */
internal fun <T, R> Response<T>.toWeatherResult(processBody: (T) -> R): WeatherResult<R> {
    return if (isSuccessful) {
        body()?.let { Success(processBody(it)) } ?: WeatherResult.Error.UnknownError
    } else {
        when (code()) {
            401 -> WeatherResult.Error.Unauthorized // Should not happen here, but just in case
            404 -> WeatherResult.Error.NotFound
            429 -> WeatherResult.Error.RateLimited
            in 500..599 -> WeatherResult.Error.ServerError
            else -> WeatherResult.Error.ApiError(code(), message())
        }
    }
}
