package com.youllbecold.trustme.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.youllbecold.trustme.ui.components.WeatherCard
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.ui.viewmodels.HomeUiState
import com.youllbecold.trustme.ui.viewmodels.HomeViewModel
import com.youllbecold.trustme.ui.viewmodels.WeatherStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreenRoot(
    viewmodel: HomeViewModel = koinViewModel()
) {
    HomeScreen(
        viewmodel.uiState,
        viewmodel::refreshLocationAndWeather,
    )
}

/**
 * Home screen.
 */
@Composable
fun HomeScreen(
    uiState: StateFlow<HomeUiState>,
    refreshWeather: () -> Unit,
) {
    val state by uiState.collectAsStateWithLifecycle(HomeUiState())
    if (!state.hasPermission) {
        return
    }

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = state.isRefreshing())

    SwipeRefresh(
        modifier = Modifier.fillMaxWidth(),
        state = swipeRefreshState,
        swipeEnabled = true,
        onRefresh = { refreshWeather() }, // Trigger refresh when user pulls to refresh
    ) {
        // SwipeRefresh needs scrollable content to function
        LazyColumn {
            item {
                AnimatedVisibility(
                    visible = state.currentWeather != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    state.currentWeather?.let {
                        WeatherCard(
                            weather = it,
                            city = state.city,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        if (state.status == WeatherStatus.Error) {
            // TODO: Some error state
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    YoullBeColdTheme {
        HomeScreen(
            MutableStateFlow(
                HomeUiState()
            ),
            refreshWeather = {},
        )
    }
}
