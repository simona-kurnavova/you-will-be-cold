package com.youllbecold.trustme.common.ui.model.status

import androidx.compose.runtime.Stable

/**
 * General UI status for screens containing progress states and helper functions.
 */
@Stable
enum class LoadingStatus {
    Idle,
    Loading,
    NoInternet,
    GenericError,
    MissingPermission,
    Success;

    /**
     * Returns true if status is error.
     */
    fun isError(): Boolean = this in listOf(GenericError, NoInternet, MissingPermission)

    /**
     * Returns true if status is Idle.
     */
    fun isIdle(): Boolean = this == Idle

    /**
     * Returns true if status is Loading.
     */
    fun isLoading(): Boolean = this == Loading

    /**
     * Returns true if status is Success.
     */
    fun isSuccess(): Boolean = this == Success
}
