package com.youllbecold.trustme.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.OutlinedCard
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
            HourlyWeatherItem(hourlyTemperatures[index])
        }
    }
}

@Composable
private fun HourlyWeatherItem(
    hourlyTemperature: HourlyTemperature,
    modifier: Modifier = Modifier
) {
    OutlinedCard {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(ITEM_PADDING.dp)
        ) {
            Icon(
                painter = painterResource(id = hourlyTemperature.weatherEvaluation.icon()),
                contentDescription = null,
                modifier = Modifier.size(ITEM_ICON_SIZE.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.padding(SPACE_INSIDE_ITEM.dp))

            Text(
                text = stringResource(
                    R.string.temperature_degrees_short,
                    hourlyTemperature.roundedTemperature
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.padding(SPACE_INSIDE_ITEM.dp))

            Text(
                text = hourlyTemperature.formattedTime,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

private const val SPACE_INSIDE_ITEM = 2
private const val ITEM_ICON_SIZE = 24
private const val ITEM_PADDING = 4

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