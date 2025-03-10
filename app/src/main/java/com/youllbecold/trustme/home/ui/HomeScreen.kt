package com.youllbecold.trustme.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.recomendation.model.Certainty
import com.youllbecold.recomendation.model.RainRecommendation
import com.youllbecold.recomendation.model.UvRecommendation
import com.youllbecold.trustme.common.ui.components.animation.FadingItem
import com.youllbecold.trustme.home.ui.components.ErrorCard
import com.youllbecold.trustme.home.ui.components.ErrorCardType
import com.youllbecold.trustme.common.ui.model.recommendation.Recommendation
import com.youllbecold.trustme.common.ui.model.status.LoadingStatus
import com.youllbecold.trustme.common.ui.model.weatherwithrecommend.WeatherWithRecommendation
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.home.ui.components.HomeContent
import com.youllbecold.trustme.home.ui.model.Forecast
import com.youllbecold.trustme.home.ui.model.HomeUiState
import com.youllbecold.weather.model.Weather
import com.youllbecold.weather.model.WeatherEvaluation
import kotlinx.collections.immutable.persistentListOf
import org.koin.androidx.compose.koinViewModel

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
    val scrollState = rememberScrollState()

    SwipeRefresh(
        state = swipeRefreshState,
        swipeEnabled = true,
        onRefresh = { onAction(HomeAction.RefreshWeather) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),  // Note: SwipeRefresh needs scrollable content to function
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                state.isInitialLoading() ->
                    CircularProgressIndicator(
                        modifier = Modifier.padding(top = PROGRESS_INDICATOR_PADDING.dp)
                    )

                state.isError() ->
                    FadingItem(visible = state.isError()) {
                        ErrorCard(
                            errorCardType = when (state.status) {
                                LoadingStatus.NoInternet -> ErrorCardType.OFFLINE
                                else -> ErrorCardType.GENERIC
                            },
                            modifier = Modifier.padding(bottom = PADDING_BETWEEN_ITEMS.dp)
                        )
                    }

                else ->
                    FadingItem(visible = state.forecast != null) {
                        HomeContent(
                            forecast = state.forecast,
                            hourlyTemperatures = state.hourlyTemperature,
                            city = state.city
                        )
                    }
            }

            Spacer(modifier = Modifier.height(END_SPACE.dp))
        }
    }
}

private const val PADDING_BETWEEN_ITEMS = 8
private const val PROGRESS_INDICATOR_PADDING = 32
private const val END_SPACE = 48

@Preview
@Composable
private fun HomeScreenPreview() {
    val weather = Weather(
        time = 1000,
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
                status = LoadingStatus.Loading,
                forecast = Forecast(
                    current = WeatherWithRecommendation(
                        weather = persistentListOf(weather, weather, weather),
                        recommendation = Recommendation(
                            uvLevel = UvRecommendation.LowProtection,
                            rainLevel = RainRecommendation.MediumRain,
                            clothes = persistentListOf(Clothes.JEANS, Clothes.LONG_SLEEVE, Clothes.TENNIS_SHOES),
                            Certainty.High
                        )
                    ),
                    today = WeatherWithRecommendation(
                        weather = persistentListOf(weather, weather, weather),
                        recommendation = Recommendation(
                            uvLevel = UvRecommendation.HighProtection,
                            rainLevel = RainRecommendation.HeavyRain,
                            clothes = persistentListOf(Clothes.JEANS, Clothes.LONG_SLEEVE, Clothes.TENNIS_SHOES),
                            Certainty.Low
                        )
                    ),
                    tomorrow = WeatherWithRecommendation(
                        weather = persistentListOf(weather, weather, weather),
                        recommendation = Recommendation(
                            uvLevel = UvRecommendation.LowProtection,
                            rainLevel = RainRecommendation.NoRain,
                            clothes = persistentListOf(Clothes.JEANS, Clothes.LONG_SLEEVE, Clothes.TENNIS_SHOES),
                            Certainty.Medium
                        )
                    )
                ),
                city = "Berlin",
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
