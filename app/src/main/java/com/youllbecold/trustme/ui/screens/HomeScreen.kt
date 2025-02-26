package com.youllbecold.trustme.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.recomendation.model.Certainty
import com.youllbecold.recomendation.model.RainRecommendation
import com.youllbecold.recomendation.model.UvRecommendation
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.cards.ErrorCard
import com.youllbecold.trustme.ui.components.cards.ErrorCardType
import com.youllbecold.trustme.ui.components.cards.HourlyWeatherCard
import com.youllbecold.trustme.ui.components.cards.RecommendationCard
import com.youllbecold.trustme.ui.components.cards.WeatherCard
import com.youllbecold.trustme.ui.components.generic.chips.ChipSelectCard
import com.youllbecold.trustme.ui.components.generic.animation.FadingItem
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.ui.viewmodels.Forecast
import com.youllbecold.trustme.ui.viewmodels.HomeAction
import com.youllbecold.trustme.ui.viewmodels.HomeUiState
import com.youllbecold.trustme.ui.viewmodels.HomeViewModel
import com.youllbecold.trustme.ui.viewmodels.state.LoadingStatus
import com.youllbecold.trustme.ui.viewmodels.state.WeatherWithRecommendation
import com.youllbecold.trustme.usecases.recommendation.Recommendation
import com.youllbecold.weather.model.Weather
import com.youllbecold.weather.model.WeatherEvaluation
import kotlinx.collections.immutable.persistentListOf
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
                .verticalScroll(scrollState)  // Note: SwipeRefresh needs scrollable content to function
        ) {
            if (state.isInitialLoading()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = PROGRESS_INDICATOR_PADDING.dp)
                )
            }

            FadingItem(visible = state.isError()) {
                ErrorCard(
                    errorCardType = when (state.status) {
                        LoadingStatus.NoInternet -> ErrorCardType.OFFLINE
                        else -> ErrorCardType.GENERIC
                    },
                    modifier = Modifier.padding(bottom = PADDING_BETWEEN_ITEMS.dp)
                )
            }

            val showWeather = state.weather != null

            FadingItem(visible = showWeather) {
                WeatherNowSection(
                    currentWeather = state.weather?.current?.weather?.first(),
                    city = state.city,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = PADDING_BETWEEN_ITEMS.dp)
                )
            }

            FadingItem(visible = showWeather) {
                state.weather?.next24Hours()?.let { hourlyTemperatures ->
                    HourlyWeatherCard(
                        hourlyTemperatures = hourlyTemperatures,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = PADDING_BETWEEN_ITEMS.dp)
                    )
                }
            }

            FadingItem(visible = showWeather) {
                state.weather?.let { weather ->
                    RecommendSection(
                        weather = weather,
                        modifier = Modifier.padding(bottom = PADDING_BETWEEN_ITEMS.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(END_SPACE.dp))
        }
    }
}

private const val CONTENT_PADDING = 8
private const val PADDING_BETWEEN_ITEMS = 8
private const val PROGRESS_INDICATOR_PADDING = 32
private const val END_SPACE = 48

@Composable
private fun WeatherNowSection(
    currentWeather: Weather?,
    city: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Current weather",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = PADDING_BETWEEN_ITEMS.dp)
        )

        currentWeather?.let {
            WeatherCard(
                weather = it,
                city = city,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = PADDING_BETWEEN_ITEMS.dp)
            )
        }
    }
}

@Composable
private fun RecommendSection(
    weather: Forecast,
    modifier: Modifier = Modifier,
) {
    var selectedOption by remember { mutableIntStateOf(RecommendationChip.NOW.ordinal) }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Recommendations",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = PADDING_BETWEEN_ITEMS.dp)
        )

        ChipSelectCard(
            options = RecommendationChip.entries.map { stringResource(it.stringId) },
            onOptionSelected = { selectedOption = it },
            selectedOption = selectedOption,
            modifier = Modifier.padding(bottom = PADDING_BETWEEN_ITEMS.dp)
        ) { page ->
            val weather = when (RecommendationChip.entries[page]) {
                RecommendationChip.NOW -> weather.current
                RecommendationChip.TODAY -> weather.today
                RecommendationChip.TOMORROW -> weather.tomorrow
            }

            RecommendationCard(
                weatherWithRecommendation = weather,
            )
        }
    }
}

enum class RecommendationChip(@StringRes val stringId: Int) {
    NOW(R.string.recommendation_chip_now),
    TODAY(R.string.recommendation_chip_today),
    TOMORROW(R.string.recommendation_chip_tomorrow)
}

@Preview
@Composable
fun HomeScreenPreview() {
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
                status = LoadingStatus.Loading,
                weather = Forecast(
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
