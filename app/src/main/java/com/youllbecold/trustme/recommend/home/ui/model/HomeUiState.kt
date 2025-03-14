package com.youllbecold.trustme.recommend.home.ui.model

import androidx.compose.runtime.Stable
import com.youllbecold.trustme.common.ui.model.status.Idle
import com.youllbecold.trustme.common.ui.model.status.Status
import com.youllbecold.trustme.common.ui.model.status.isError
import com.youllbecold.trustme.common.ui.model.status.isLoading
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

/**
 * UI state for the home screen.
 *
 * @param status The current status of the weather fetching, location refreshing, etc.
 * @param city The city name.
 * @param forecast Forecast for the current, today, and tomorrow weather, with recommendations.
 */
@Stable
data class HomeUiState(
    val status: Status = Idle,
    val city: String? = null,
    val forecast: Forecast? = null,
    val hourlyTemperature: PersistentList<HourlyTemperature> = persistentListOf()
) {
    /**
     * Whether the screen is refreshing - does not account for the initial loading.
     */
    fun isRefreshing() = status.isLoading() && forecast != null

    /**
     * Whether the screen is loading for the first time.
     */
    fun isInitialLoading() = status.isLoading() && forecast == null

    /**
     * Whether the screen is in an error state.
     */
    fun isError() = status.isError()
}
