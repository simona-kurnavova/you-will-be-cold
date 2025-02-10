package com.youllbecold.trustme.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.IconText
import com.youllbecold.trustme.ui.components.generic.OutlinedCard
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
                if (city != null) {
                    CityView(
                        city = city,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    )
                }

                Spacer(modifier = Modifier.padding(2.dp))

                CurrentTemperatureView(
                    temperature = weather.temperature,
                    useCelsius = weather.unitsCelsius,
                    icon = weather.weatherEvaluation.icon(),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onBackground,
                    thickness = 0.1.dp,
                    modifier = Modifier.padding(8.dp),
                )

                WeatherParametersView(
                    windSpeed = weather.windSpeed,
                    precipitationProbability = weather.precipitationProbability,
                    uvIndex = weather.uvIndex,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }
        }
    }
}

@Composable
fun CityView(
    city: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            modifier = Modifier
                .size(12.dp)
                .align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.padding(2.dp))

        Text(
            text = city,
            style = MaterialTheme.typography.bodySmall,
            modifier = modifier
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun CurrentTemperatureView(
    temperature: Double,
    useCelsius: Boolean,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Row(
        modifier = modifier,
    ) {
        val temperatureWithUnits = context.getTemperatureString(temperature, useCelsius)

        Text(
            text = temperatureWithUnits,
            style = MaterialTheme.typography.headlineLarge,
            fontSize = 64.sp,
            modifier = modifier
                .align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.padding(4.dp))

        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.CenterVertically),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun WeatherParametersView(
    windSpeed: Double,
    precipitationProbability: Int,
    uvIndex: Double,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        IconText(
            icon = R.drawable.ic_wind,
            text = windSpeed.toString(),
        )

        Spacer(modifier = Modifier.padding(SPACER_PADDING.dp))

        IconText(
            icon = R.drawable.ic_rain,
            text = precipitationProbability.toString(),
        )

        Spacer(modifier = Modifier.padding(SPACER_PADDING.dp))

        IconText(
            icon = R.drawable.ic_sun,
            text = uvIndex.toString(),
        )
    }
}

private const val SPACER_PADDING = 8

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
