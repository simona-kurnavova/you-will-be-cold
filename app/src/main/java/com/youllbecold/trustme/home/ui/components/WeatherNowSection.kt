package com.youllbecold.trustme.home.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.components.themed.ThemedText
import com.youllbecold.weather.model.Weather

@Composable
fun WeatherNowSection(
    currentWeather: Weather?,
    city: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        ThemedText(
            text = stringResource(R.string.home_section_current_weather),
            modifier = Modifier.padding(vertical = PADDING_BETWEEN_ITEMS.dp)
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

private const val PADDING_BETWEEN_ITEMS = 8

// TODO: preview