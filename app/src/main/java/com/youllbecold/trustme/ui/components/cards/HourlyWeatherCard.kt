package com.youllbecold.trustme.ui.components.cards

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
import com.youllbecold.trustme.ui.components.generic.IconType
import com.youllbecold.trustme.ui.components.generic.icontext.Tile
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.ui.viewmodels.HourlyTemperature
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun HourlyWeatherCard(
    hourlyTemperatures: PersistentList<HourlyTemperature>,
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
                    hourItem.temperature
                ),
                iconType = hourItem.weatherIcon,
                subtitle = hourItem.formattedTime,
            )

            if (index != hourlyTemperatures.size - 1) {
                Spacer(modifier = Modifier.size(SPACE_BETWEEN_ITEMS.dp))
            }
        }
    }
}

private const val SPACE_BETWEEN_ITEMS = 8

@Preview(showBackground = true)
@Composable
private fun HourlyWeatherCardPreview() {
    val item = HourlyTemperature(100000, 5, IconType.Sun)

    YoullBeColdTheme {
        HourlyWeatherCard(
            persistentListOf(item, item, item, item)
        )
    }
}
