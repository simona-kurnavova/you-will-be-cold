package com.youllbecold.trustme.recommend.home.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.recommend.home.ui.model.Forecast
import com.youllbecold.trustme.recommend.home.ui.model.HourlyTemperature
import kotlinx.collections.immutable.PersistentList

@Composable
fun HomeContent(
    forecast: Forecast?,
    hourlyTemperatures: PersistentList<HourlyTemperature>,
    city: String?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        WeatherNowSection(
            currentWeather = forecast?.current?.weather?.first(),
            city = city,
            modifier = Modifier
                .padding(bottom = PADDING_BETWEEN_ITEMS.dp)
                .padding(horizontal = HORIZONTAL_SCREEN_PADDING.dp)
        )

        HourlyWeatherCard(
            hourlyTemperatures = hourlyTemperatures,
            modifier = Modifier
                .padding(bottom = PADDING_BETWEEN_ITEMS.dp)
                .padding(horizontal = HORIZONTAL_SCREEN_PADDING.dp)
        )

        forecast?.let { weather ->
            RecommendSection(
                weather = weather,
                horizontalPadding = HORIZONTAL_SCREEN_PADDING,
                modifier = Modifier.padding(bottom = PADDING_BETWEEN_ITEMS.dp),
            )
        }
    }
}

private const val HORIZONTAL_SCREEN_PADDING = 12
private const val PADDING_BETWEEN_ITEMS = 8
