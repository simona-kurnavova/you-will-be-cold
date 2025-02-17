package com.youllbecold.trustme.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.youllbecold.trustme.ui.components.cards.HourlyWeatherCard
import com.youllbecold.trustme.ui.components.cards.ErrorCard
import com.youllbecold.trustme.ui.components.cards.ErrorCardType
import com.youllbecold.trustme.ui.components.cards.WeatherCard
import com.youllbecold.trustme.ui.components.generic.animation.FadingItem
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.ui.viewmodels.HomeAction
import com.youllbecold.trustme.ui.viewmodels.HomeUiState
import com.youllbecold.trustme.ui.viewmodels.HomeViewModel
import com.youllbecold.trustme.ui.viewmodels.HourlyTemperature
import com.youllbecold.trustme.ui.viewmodels.LoadingStatus
import com.youllbecold.trustme.ui.viewmodels.isError
import com.youllbecold.weather.model.Weather
import com.youllbecold.weather.model.WeatherEvaluation
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime

@Composable
fun HomeScreenRoot(
    viewmodel: HomeViewModel = koinViewModel()
) {
    HomeScreen(
        viewmodel.uiState.collectAsStateWithLifecycle(),
        viewmodel::onAction,
    )
}

/**
 * Home screen.
 */
@Composable
private fun HomeScreen(
    uiState: State<HomeUiState>,
    onAction: (HomeAction) -> Unit,
) {
    val state = uiState.value

    if (!state.hasPermission) {
        return
    }

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = state.isRefreshing())

    SwipeRefresh(
        modifier = Modifier.fillMaxWidth(),
        state = swipeRefreshState,
        swipeEnabled = true,
        onRefresh = { onAction(HomeAction.RefreshWeather) }
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(CONTENT_PADDING.dp)
                .verticalScroll(scrollState)  // Note: SwipeRefresh needs scrollable content to function
        ) {
            FadingItem(visible = state.status.isError()) {
                ErrorCard(
                    errorCardType = when (state.status) {
                        LoadingStatus.NoInternet -> ErrorCardType.OFFLINE
                        else -> ErrorCardType.GENERIC
                    },
                    modifier = Modifier.padding(bottom = PADDING_BETWEEN_ITEMS.dp)
                )
            }

            val showWeather = state.currentWeather != null

            FadingItem(visible = showWeather) {
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

            FadingItem(visible = showWeather) {
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
}

private const val CONTENT_PADDING = 8
private const val PADDING_BETWEEN_ITEMS = 8

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
    
    val state = remember {
        mutableStateOf(
            HomeUiState(
                hasPermission = true,
                status = LoadingStatus.NoInternet,
                currentWeather = weather,
                hourlyTemperatures = listOf(hourlyTemperature, hourlyTemperature, hourlyTemperature),
            )
        )
    }

    YoullBeColdTheme {
        HomeScreen(
            uiState = state,
            onAction = {},
        )
    }
}
