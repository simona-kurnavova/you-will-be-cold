package com.youllbecold.trustme.recommend.home.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.components.themed.ThemedText
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme
import com.youllbecold.weather.model.Weather
import com.youllbecold.weather.model.WeatherEvaluation

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

@Preview
@Composable
private fun WeatherNowSectionPreview() {
    YoullBeColdTheme {
        WeatherNowSection(
            currentWeather = Weather(
                time = 1000,
                unitsCelsius = true,
                temperature = -10.0,
                apparentTemperature = 20.0,
                weatherEvaluation = WeatherEvaluation.CLOUDY,
                relativeHumidity = 50,
                windSpeed = 10.0,
                precipitationProbability = 0,
                uvIndex = 0.0,
            ),
            city = "London"
        )
    }
}
