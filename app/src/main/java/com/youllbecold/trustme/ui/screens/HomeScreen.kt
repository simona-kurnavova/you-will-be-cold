package com.youllbecold.trustme.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.youllbecold.trustme.ui.components.WeatherCard
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.weatherservice.model.WeatherNow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Home screen.
 */
@Composable
fun HomeScreen(
    locationGranted: StateFlow<Boolean>,
    weatherState: StateFlow<WeatherNow?>,
    refreshWeather: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val locationState = locationGranted.collectAsStateWithLifecycle(null)
    if (locationState.value != true) {
        return
    }

    val state = weatherState.collectAsStateWithLifecycle(null)

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = state.value == null)

    SwipeRefresh(
        modifier = modifier.fillMaxWidth(),
        state = swipeRefreshState,
        swipeEnabled = true,
        onRefresh = { refreshWeather() }, // Trigger refresh when user pulls to refresh
    ) {
        // SwipeRefresh needs scrollable content to function
        LazyColumn {
            item {
                state.value?.let { weather ->
                    WeatherCard(
                        weather = weather,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    YoullBeColdTheme {
        HomeScreen(
            MutableStateFlow(true),
            MutableStateFlow(null),
            refreshWeather = {},
        )
    }
}
