package com.youllbecold.trustme.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.IconRowData
import com.youllbecold.trustme.ui.components.generic.IconText
import com.youllbecold.trustme.ui.components.generic.IconTextRow
import com.youllbecold.trustme.ui.components.generic.OutlinedCard
import com.youllbecold.trustme.ui.components.generic.attributes.IconAttr
import com.youllbecold.trustme.ui.components.utils.rememberVector
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.ui.utils.getTemperatureString
import com.youllbecold.trustme.ui.utils.icon
import com.youllbecold.trustme.ui.utils.thermometerImage
import com.youllbecold.weather.model.Weather
import com.youllbecold.weather.model.WeatherEvaluation
import java.time.LocalDateTime

@Composable
fun WeatherCard(
    weather: Weather,
    city: String?,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(modifier = modifier) {
        Row {
            Image(
                painter = painterResource(id = weather.thermometerImage()),
                contentDescription = null,
            )

            Column {
                city?.let {
                    IconText(
                        text = city,
                        icon = rememberVectorPainter(Icons.Default.LocationOn),
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    )
                }

                Spacer(modifier = Modifier.width(SPACER_UNDER_CITY.dp))

                CurrentTemperatureView(
                    temperature = weather.temperature,
                    useCelsius = weather.unitsCelsius,
                    icon = weather.weatherEvaluation.icon(),
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
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }
        }
    }
}

private const val DIVIDER_THICKNESS = 0.1f
private const val DIVIDER_PADDING = 8
private const val SPACER_UNDER_CITY = 4

@Composable
fun CurrentTemperatureView(
    temperature: Double,
    useCelsius: Boolean,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
    ) {
        val temperatureWithUnits = LocalContext.current
            .getTemperatureString(temperature, useCelsius)

        Text(
            text = temperatureWithUnits,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = TEMPERATURE_FONT_SIZE.sp,
            modifier = modifier.align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.width(SPACER_WIDTH.dp))

        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .size(IconAttr.BIG_ICON_SIZE.dp)
                .align(Alignment.CenterVertically),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

private const val TEMPERATURE_FONT_SIZE = 64
private const val SPACER_WIDTH = 8

@Composable
fun WeatherParameters(
    windSpeed: Double,
    precipitationProbability: Int,
    uvIndex: Double,
    modifier: Modifier = Modifier,
) {
    IconTextRow(
        items = listOf(
            IconRowData(
                painter = rememberVector(R.drawable.ic_wind),
                text = windSpeed.toString(),
            ),
            IconRowData(
                painter = rememberVector(R.drawable.ic_rain),
                text = precipitationProbability.toString(),
            ),
            IconRowData(
                painter = rememberVector(R.drawable.ic_sun),
                text = uvIndex.toString(),
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
                time = LocalDateTime.now(),
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
