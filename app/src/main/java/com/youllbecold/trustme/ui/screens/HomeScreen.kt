package com.youllbecold.trustme.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    currentWeather: StateFlow<WeatherNow?>,
    modifier: Modifier = Modifier,
    refreshWeather: () -> Unit,
) {
    val locationState = locationGranted.collectAsStateWithLifecycle(null)
    if (locationState.value != true) {
        return
    }

    val state = currentWeather.collectAsStateWithLifecycle(null)

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        state.value?.let { weather ->
            WeatherCard(
                weather = weather,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.padding(8.dp))

        Button(
            onClick = {
                refreshWeather()
            },
        ) {
            Text(text = "Refresh")
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    YoullBeColdTheme {
        HomeScreen(
            MutableStateFlow(true),
            MutableStateFlow(null)
        ) {}
    }
}
