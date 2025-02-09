package com.youllbecold.trustme.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.Tile
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.ui.utils.icon
import com.youllbecold.trustme.ui.viewmodels.HourlyTemperature
import com.youllbecold.weather.model.WeatherEvaluation
import java.time.LocalDateTime

@Composable
fun HourlyWeatherCard(
    hourlyTemperatures: List<HourlyTemperature>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.wrapContentHeight(),
    ) {
        items(hourlyTemperatures.size) { index ->
            val hourItem = hourlyTemperatures[index]

            Tile(
                title = stringResource(
                    R.string.temperature_degrees_short,
                    hourItem.roundedTemperature
                ),
                icon = hourItem.weatherEvaluation.icon(),
                subtitle = hourItem.formattedTime,
            )

            if (index != hourlyTemperatures.size - 1) {
                Spacer(modifier = Modifier.size(SPACE_BETWEEN_ITEMS.dp))
            }
        }
    }
}

private const val SPACE_BETWEEN_ITEMS = 4

@Preview(showBackground = true)
@Composable
private fun HourlyWeatherCardPreview() {
    val item = HourlyTemperature(
        LocalDateTime.now(),
        3.5,
        WeatherEvaluation.CLOUDY
    )
    YoullBeColdTheme {
        HourlyWeatherCard(
            listOf(item, item, item, item)
        )
    }
}
