package com.youllbecold.trustme.ui.viewmodels.state

/**
 * General UI status for screens.
 */
enum class LoadingStatus {
    Idle,
    Loading,
    NoInternet,
    GenericError,
}

/**
 * Returns true if status is error.
 */
fun LoadingStatus.isError() = this in listOf(LoadingStatus.GenericError, LoadingStatus.NoInternet)

/**
 * Returns true if status is Idle.
 */
fun LoadingStatus.isIdle() = this == LoadingStatus.Idle
