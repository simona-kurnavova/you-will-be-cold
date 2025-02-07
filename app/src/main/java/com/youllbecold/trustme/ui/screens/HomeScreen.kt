package com.youllbecold.trustme.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.youllbecold.trustme.ui.components.HourlyWeatherCard
import com.youllbecold.trustme.ui.components.WeatherCard
import com.youllbecold.trustme.ui.components.generic.FadingItem
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.ui.viewmodels.HomeAction
import com.youllbecold.trustme.ui.viewmodels.HomeUiState
import com.youllbecold.trustme.ui.viewmodels.HomeViewModel
import com.youllbecold.trustme.ui.viewmodels.HourlyTemperature
import com.youllbecold.trustme.ui.viewmodels.WeatherStatus
import com.youllbecold.weather.model.WeatherEvaluation
import com.youllbecold.weather.model.Weather
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime

@Composable
fun HomeScreenRoot(
    viewmodel: HomeViewModel = koinViewModel()
) {
    HomeScreen(
        viewmodel.uiState,
        viewmodel::onAction,
    )
}

/**
 * Home screen.
 */
@Composable
fun HomeScreen(
    uiState: StateFlow<HomeUiState>,
    onAction: (HomeAction) -> Unit,
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
        onRefresh = {
            // Trigger refresh when user pulls to refresh
            onAction(HomeAction.RefreshWeather)
        }
    ) {
        // SwipeRefresh needs scrollable content to function
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(CONTENT_PADDING.dp)
        ) {
            item {
                FadingItem(visible = state.currentWeather != null) {
                    state.currentWeather?.let {
                        WeatherCard(
                            weather = it,
                            city = state.city,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = PADDING_BETWEEN_ITEMS.dp)
                        )
                    }
                }
            }

            item {
                FadingItem(visible = state.currentWeather != null,) {
                    state.currentWeather?.let {
                        HourlyWeatherCard(
                            hourlyTemperatures = state.hourlyTemperatures,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = PADDING_BETWEEN_ITEMS.dp)
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

private const val CONTENT_PADDING = 8
private const val PADDING_BETWEEN_ITEMS = 4

@Preview
@Composable
fun HomeScreenPreview() {
    val hourlyTemperature = HourlyTemperature(
        LocalDateTime.now(),
        0.0,
        WeatherEvaluation.SUNNY
    )

    val weather = Weather(
        time = LocalDateTime.now(),
        unitsCelsius = true,
        temperature = 2.0,
        apparentTemperature = 20.0,
        weatherEvaluation = WeatherEvaluation.CLOUDY,
        relativeHumidity = 1,
        windSpeed = 5.0,
        precipitationProbability = 2,
        uvIndex = 5.0,
    )

    YoullBeColdTheme {
        HomeScreen(
            uiState = MutableStateFlow(HomeUiState(
                hasPermission = true,
                status = WeatherStatus.Idle,
                currentWeather = weather,
                hourlyTemperatures = listOf(hourlyTemperature, hourlyTemperature, hourlyTemperature),
            )),
            onAction = {},
        )
    }
}
