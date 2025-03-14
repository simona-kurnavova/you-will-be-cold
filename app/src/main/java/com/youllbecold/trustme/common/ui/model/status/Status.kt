package com.youllbecold.trustme.common.ui.model.status

import androidx.compose.runtime.Stable

/**
 * General UI status for screens/viewmodels containing progress states and helper functions.
 */
@Stable
sealed interface Status

object Idle : Status
object Loading : Status
object Success : Status

sealed interface Error : Status {
    object NoInternet : Error
    object MissingPermission : Error
    object LocationMissing : Error
    object ApiError : Error
    object GenericError : Error
}

/**
 * Returns true if status is error.
 */
fun Status.isError(): Boolean = this is Error

/**
 * Returns true if status is Idle.
 */
fun Status.isIdle(): Boolean = this is Idle

/**
 * Returns true if status is Loading.
 */
fun Status.isLoading(): Boolean = this is Loading

/**
 * Returns true if status is Success.
 */
fun Status.isSuccess(): Boolean = this is Success
