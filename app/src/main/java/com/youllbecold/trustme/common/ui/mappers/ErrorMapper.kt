package com.youllbecold.trustme.common.ui.mappers

import com.youllbecold.trustme.common.ui.model.status.Error
import com.youllbecold.weather.api.WeatherResult

/**
 * Maps a weather result error to a common error.
 *
 * Note: Could this be more robust? Yeah, it could, but that would take way too many error messages, so oh well.
 */
fun WeatherResult.Error.toError(): Error = when(this) {
    WeatherResult.Error.NoInternet -> Error.NoInternet
    WeatherResult.Error.UnknownError -> Error.GenericError
    is WeatherResult.Error.ApiError,
    WeatherResult.Error.NotFound,
    WeatherResult.Error.RateLimited,
    WeatherResult.Error.ServerError,
    WeatherResult.Error.Unauthorized ->  Error.ApiError
}