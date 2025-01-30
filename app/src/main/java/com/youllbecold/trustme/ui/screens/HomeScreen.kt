package com.youllbecold.trustme.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.youllbecold.trustme.model.WeatherStats
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Home screen.
 */
@Composable
fun HomeScreen(
    currentWeather: StateFlow<WeatherStats?>,
    modifier: Modifier = Modifier
) {
    val state = currentWeather.collectAsStateWithLifecycle(null)

    Text(
        text = "Weather = ${state.value}",
        modifier = modifier
    )
}

@Preview
@Composable
fun HomeScreenPreview() {
    YoullBeColdTheme {
        HomeScreen(MutableStateFlow(null))
    }
}
