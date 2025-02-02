package com.youllbecold.trustme.ui.components

import androidx.annotation.DrawableRes
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.OutlinedCard
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.weatherservice.model.WeatherEvaluation
import com.youllbecold.trustme.weatherservice.model.WeatherNow

@Composable
fun WeatherCard(
    weather: WeatherNow,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(modifier = modifier) {
        Column {
            if (weather.city != null) {
                CityView(
                    city = weather.city,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }

            Spacer(modifier = Modifier.padding(2.dp))

            CurrentTemperatureView(
                temperature = weather.temperature,
                useCelsius = weather.unitsCelsius,
                icon = weather.weatherEvaluation.resolveIcon(),
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
        val temperatureWithUnits = if (useCelsius) {
            context.getString(R.string.temperature_celsius, temperature)
        } else {
            context.getString(R.string.temperature_fahrenheit, temperature)
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
                .align(Alignment.CenterVertically),
            tint = MaterialTheme.colorScheme.secondary
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

        Spacer(modifier = Modifier.padding(8.dp))

        IconText(
            icon = R.drawable.ic_rain,
            text = precipitationProbability.toString(),
        )

        Spacer(modifier = Modifier.padding(8.dp))

        IconText(
            icon = R.drawable.ic_sun,
            text = uvIndex.toString(),
        )
    }

}

@Composable
fun IconText(
    @DrawableRes icon: Int,
    text: String,
) {
    Row {
        Icon(
            imageVector = ImageVector.vectorResource(icon),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.padding(2.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@DrawableRes
private fun WeatherEvaluation.resolveIcon(): Int = when (this) {
    WeatherEvaluation.SUNNY -> R.drawable.ic_sun
    WeatherEvaluation.CLOUDY -> R.drawable.ic_cloud
    WeatherEvaluation.RAINY -> R.drawable.ic_rain
    WeatherEvaluation.SNOWY -> R.drawable.ic_snow
    WeatherEvaluation.STORM -> R.drawable.ic_lightning
    WeatherEvaluation.FOGGY -> R.drawable.ic_cloud
    WeatherEvaluation.UNKNOWN -> R.drawable.ic_cloud
}

@Preview(showBackground = true)
@Composable
private fun WeatherCardPreview() {
    YoullBeColdTheme {
        WeatherCard(
            weather = WeatherNow(
                city = "Prague",
                unitsCelsius = true,
                temperature = 20.0,
                apparentTemperature = 20.0,
                weatherEvaluation = WeatherEvaluation.SUNNY,
                relativeHumidity = 50,
                windSpeed = 10.0,
                precipitationProbability = 0,
                uvIndex = 0.0,
            ),
            modifier = Modifier.padding(4.dp),
        )
    }
}
