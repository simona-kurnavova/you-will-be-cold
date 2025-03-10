package com.youllbecold.trustme.common.domain.usecases.weather.model

/**
 * General status for weather usecases.
 */
sealed class WeatherUseCaseStatus {
    data object Idle : WeatherUseCaseStatus()
    data object Loading : WeatherUseCaseStatus()
    data object Success : WeatherUseCaseStatus()
    data class Error(val type: ErrorType) : WeatherUseCaseStatus()
}
