package com.youllbecold.trustme.usecases.weather.state

import com.youllbecold.weather.api.isSuccessful

data class WeatherState<T>(
    val status: WeatherUseCaseStatus,
    val weather: T,
)

fun <T> WeatherState<T>.copyWithNetworkResult(
    result: Result<T>,
): WeatherState<T> =
     when {
        result.isSuccessful -> copy(
            status = WeatherUseCaseStatus.Success,
            weather = result.getOrThrow(),
        )
        else -> copy(
            status = WeatherUseCaseStatus.Error(ErrorType.UNKNOWN)
        )
    }

fun <T> WeatherState<T>.copyWithError(
    type: ErrorType,
): WeatherState<T> = copy(
    status = WeatherUseCaseStatus.Error(type)
)

fun <T> WeatherState<T>.copyWithLoading(): WeatherState<T> = copy(
    status = WeatherUseCaseStatus.Loading
)
