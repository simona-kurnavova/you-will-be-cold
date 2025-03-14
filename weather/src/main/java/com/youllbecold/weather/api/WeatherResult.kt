package com.youllbecold.weather.api

/**
 * Result of a weather API call.
 */
sealed class WeatherResult<out T> {

    /**
     * Success result.
     *
     * @param data The data.
     */
    data class Success<T>(val data: T) : WeatherResult<T>()

    /**
     * Error result.
     */
    sealed class Error : WeatherResult<Nothing>() {
        object NoInternet : Error()
        object Unauthorized : Error()
        object NotFound : Error()
        object ServerError : Error()
        object RateLimited : Error()
        data class ApiError(val code: Int, val message: String?) : Error()
        object UnknownError : Error()
    }
}
