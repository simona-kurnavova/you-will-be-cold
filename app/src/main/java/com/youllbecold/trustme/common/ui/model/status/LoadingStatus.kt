package com.youllbecold.trustme.common.ui.model.status

/**
 * General UI status for screens.
 */
enum class LoadingStatus {
    Idle,
    Loading,
    NoInternet,
    GenericError;

    /**
     * Returns true if status is error.
     */
    fun isError(): Boolean = this in listOf(GenericError, NoInternet)

    /**
     * Returns true if status is Idle.
     */
    fun isIdle(): Boolean = this == Idle

    /**
     * Returns true if status is Loading.
     */
    fun isLoading(): Boolean = this == Loading
}
