package com.youllbecold.trustme.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.weatherservice.internal.WeatherStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Home screen.
 */
@Composable
fun HomeScreen(
    locationGranted: StateFlow<Boolean>,
    currentWeather: StateFlow<WeatherStats?>,
    modifier: Modifier = Modifier
) {
    val locationState = locationGranted.collectAsStateWithLifecycle(null)
    if (locationState.value != true) {
        return
    }

    val state = currentWeather.collectAsStateWithLifecycle(null)

    Column {
        if (state.value != null) {
            /*WeatherCard(
                weather = state.value,
                modifier = modifier
            )*/
        }

    }
    Text(
        text = "Weather = ${state.value}",
        modifier = modifier
    )
}

@Preview
@Composable
fun HomeScreenPreview() {
    YoullBeColdTheme {
        HomeScreen(
            MutableStateFlow(true),
            MutableStateFlow(null)
        )
    }
}
