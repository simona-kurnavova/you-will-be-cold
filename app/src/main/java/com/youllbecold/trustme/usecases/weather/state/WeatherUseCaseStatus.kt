package com.youllbecold.trustme.usecases.weather.state

/**
 * General status for usecases.
 */
sealed class WeatherUseCaseStatus {
    data object Idle : WeatherUseCaseStatus()
    data object Loading : WeatherUseCaseStatus()
    data object Success : WeatherUseCaseStatus()
    data class Error(val type: ErrorType) : WeatherUseCaseStatus()
}
