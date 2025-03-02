package com.youllbecold.trustme.ui.components.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youllbecold.trustme.ui.components.generic.icontext.IconRowData
import com.youllbecold.trustme.ui.components.generic.icontext.IconText
import com.youllbecold.trustme.ui.components.generic.icontext.IconTextRow
import com.youllbecold.trustme.ui.components.generic.IconType
import com.youllbecold.trustme.ui.components.generic.ThemedCard
import com.youllbecold.trustme.ui.components.generic.ThemedIcon
import com.youllbecold.trustme.ui.components.generic.ThemedText
import com.youllbecold.trustme.ui.components.generic.attributes.TextAttr
import com.youllbecold.trustme.ui.components.generic.attributes.defaultBigIconAttr
import com.youllbecold.trustme.ui.components.utils.rememberVector
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.ui.utils.getTemperatureString
import com.youllbecold.trustme.ui.utils.icon
import com.youllbecold.trustme.ui.utils.thermometer
import com.youllbecold.weather.model.Weather
import com.youllbecold.weather.model.WeatherEvaluation
import kotlinx.collections.immutable.persistentListOf

@Composable
fun WeatherCard(
    weather: Weather,
    city: String?,
    modifier: Modifier = Modifier,
) {
    ThemedCard(modifier = modifier) {
        Row {
            Image(
                painter = rememberVector(weather.thermometer),
                contentDescription = null,
            )

            Column {
                city?.let {
                    IconText(
                        text = city,
                        iconType = IconType.Location,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    )
                }

                Spacer(modifier = Modifier.height(SPACER_UNDER_CITY.dp))

                CurrentTemperatureView(
                    temperature = weather.temperature,
                    useCelsius = weather.unitsCelsius,
                    iconType = weather.weatherEvaluation.icon,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onBackground,
                    thickness = DIVIDER_THICKNESS.dp,
                    modifier = Modifier.padding(DIVIDER_PADDING.dp),
                )

                WeatherParameters(
                    windSpeed = weather.windSpeed,
                    precipitationProbability = weather.precipitationProbability,
                    uvIndex = weather.uvIndex,
                    humidity = weather.relativeHumidity,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }
        }
    }
}

private const val DIVIDER_THICKNESS = 0.1f
private const val DIVIDER_PADDING = 8
private const val SPACER_UNDER_CITY = 8

@Composable
private fun CurrentTemperatureView(
    temperature: Double,
    useCelsius: Boolean,
    iconType: IconType,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
    ) {
        val temperatureWithUnits = LocalContext.current
            .getTemperatureString(temperature, useCelsius)

        ThemedText(
            text = temperatureWithUnits,
            textAttr = TextAttr(
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = TEMPERATURE_FONT_SIZE.sp
                ),
                color = MaterialTheme.colorScheme.onBackground,
            ),
            modifier = modifier.align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.width(SPACER_WIDTH.dp))

        ThemedIcon(
            iconType = iconType,
            iconAttr = defaultBigIconAttr(),
            modifier = Modifier.align(Alignment.CenterVertically),
        )
    }
}

private const val TEMPERATURE_FONT_SIZE = 64
private const val SPACER_WIDTH = 8

@Composable
private fun WeatherParameters(
    windSpeed: Double,
    precipitationProbability: Int,
    uvIndex: Double,
    humidity: Int,
    modifier: Modifier = Modifier,
) {
    IconTextRow(
        items = persistentListOf(
            IconRowData(
                iconType = IconType.Wind,
                text = windSpeed.toString(),
            ),
            IconRowData(
                iconType = IconType.Rain,
                text = precipitationProbability.toString(),
            ),
            IconRowData(
                iconType = IconType.Sun,
                text = uvIndex.toString(),
            ),
            IconRowData(
                iconType = IconType.Droplet,
                text = humidity.toString(),
            ),
        ),
        modifier = modifier
    )
}


@Preview(showBackground = true)
@Composable
private fun WeatherCardPreview() {
    YoullBeColdTheme {
        WeatherCard(
            weather = Weather(
                time = 1000,
                unitsCelsius = true,
                temperature = -10.0,
                apparentTemperature = 20.0,
                weatherEvaluation = WeatherEvaluation.SUNNY,
                relativeHumidity = 50,
                windSpeed = 10.0,
                precipitationProbability = 0,
                uvIndex = 0.0,
            ),
            city = "Berlin",
        )
    }
}
