package com.youllbecold.trustme.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.model.Evaluation
import com.youllbecold.trustme.model.Temperature
import com.youllbecold.trustme.model.TemperatureUnit
import com.youllbecold.trustme.model.Weather
import com.youllbecold.trustme.ui.components.generic.OutlinedCard
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import java.util.Date

@Composable
fun WeatherCard(
    weather: Weather,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(modifier = modifier) {
        Column {
            CityView(
                city = weather.city,
            )

            CurrentTemperatureView(
                temperature = weather.currentTemperature,
                unit = weather.temperatureUnit,
                icon = weather.evaluation.resolveIcon(),
            )
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
                .size(24.dp)
                .align(Alignment.CenterVertically)
                .padding(end = 8.dp),
        )

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
    temperature: Float,
    unit: TemperatureUnit,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
    ) {
        val temperatureWithUnits = if (unit == TemperatureUnit.CELSIUS) {
            "$temperature°C"
        } else {
            "$temperature°F"
        }

        Text(
            text = temperatureWithUnits,
            style = MaterialTheme.typography.headlineLarge,
            modifier = modifier
                .align(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.padding(4.dp))

        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

@DrawableRes
private fun Evaluation.resolveIcon(): Int = when (this) {
    Evaluation.SUNNY -> R.drawable.ic_sun
    Evaluation.CLOUDY,
    Evaluation.SUNNY_CLOUDY -> R.drawable.ic_cloud
    Evaluation.RAINY -> R.drawable.ic_rain
    Evaluation.SNOWY -> R.drawable.ic_snow
    Evaluation.WINDY -> R.drawable.ic_wind
    Evaluation.NIGHT -> R.drawable.ic_moon
    Evaluation.STORM -> R.drawable.ic_lightning
}

@Preview(showBackground = true)
@Composable
private fun WeatherCardPreview() {
    YoullBeColdTheme {
        WeatherCard(
            weather = Weather(
                city = "Prague",
                elevation = 20f,
                date = Date(),
                timezone = "CET",
                timezoneAbbreviation = "CET",
                temperatureUnit = TemperatureUnit.CELSIUS,
                currentTemperature = 20.0f,
                evaluation = Evaluation.WINDY,
                hourlyTemperatures = listOf(
                    Temperature(time = "12:00", temperature = 20.0),
                    Temperature(time = "13:00", temperature = 25.0),
                    Temperature(time = "14:00", temperature = 7.0),
                    Temperature(time = "15:00", temperature = 20.0),
                    Temperature(time = "16:00", temperature = 22.0),
                    Temperature(time = "17:00", temperature = -1.0),
                    Temperature(time = "18:00", temperature = 20.0),
                ),
            ),
            modifier = Modifier.padding(4.dp),
        )
    }
}
