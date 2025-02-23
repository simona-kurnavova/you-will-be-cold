package com.youllbecold.trustme.ui.components.cards

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.recomendation.model.Certainty
import com.youllbecold.recomendation.model.RainRecommendation
import com.youllbecold.recomendation.model.UvRecommendation
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.IconType
import com.youllbecold.trustme.ui.components.generic.OutlinedCard
import com.youllbecold.trustme.ui.components.generic.ThemedDivider
import com.youllbecold.trustme.ui.components.generic.icontext.IconText
import com.youllbecold.trustme.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.ui.utils.feelLikeDescription
import com.youllbecold.trustme.ui.utils.getTitle
import com.youllbecold.trustme.ui.utils.icon
import com.youllbecold.trustme.ui.utils.temperatureRangeDescription
import com.youllbecold.trustme.ui.viewmodels.WeatherWithRecommendation
import com.youllbecold.trustme.usecases.weather.Recommendation
import com.youllbecold.weather.model.Weather
import com.youllbecold.weather.model.WeatherEvaluation
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDateTime

@Composable
fun RecommendationCard(
    weatherWithRecommendation: WeatherWithRecommendation,
    modifier: Modifier = Modifier
) {
    OutlinedCard(modifier = modifier) {
        Column {
            val recommendation = weatherWithRecommendation.recommendation

            listOfNotNull(
                weatherWithRecommendation.temperatureRangeDescription() to IconType.Thermometer,
                weatherWithRecommendation.feelLikeDescription() to IconType.Person,
                (recommendation.uvLevel.getTitle() to IconType.Sun)
                    .takeIf { recommendation.uvLevel != UvRecommendation.NoProtection },
                (recommendation.rainLevel.getTitle() to IconType.Rain)
                    .takeIf { recommendation.rainLevel != RainRecommendation.NoRain }
            ).forEach { (text, iconType) ->
                IconText(
                    text = text,
                    iconType = iconType,
                )

                ThemedDivider(
                    modifier = Modifier.padding(vertical = ITEMS_PADDING.dp)
                )
            }

            ClothesRecommendations(recommendation)

            ThemedDivider(
                modifier = Modifier.padding(vertical = ITEMS_PADDING.dp)
            )

            Text(
                text = stringResource(R.string.recom_certainty_description, recommendation.certainty.getTitle()),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun ClothesRecommendations(
    recommendation: Recommendation,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.recom_clothes_title),
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(
            modifier = Modifier.height(ITEMS_PADDING.dp)
        )

        Column{
            recommendation.clothes.forEach {
                IconText(
                    text = it.getTitle(),
                    iconType = it.icon,
                    modifier = Modifier.padding(vertical = ITEMS_PADDING.dp)
                )
            }
        }
    }
}

private const val ITEMS_PADDING = 8

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun RecommendationCardPreview() {
    val weather = Weather(
        time = LocalDateTime.now(),
        unitsCelsius = true,
        temperature = -10.0,
        apparentTemperature = 20.0,
        weatherEvaluation = WeatherEvaluation.SUNNY,
        relativeHumidity = 50,
        windSpeed = 10.0,
        precipitationProbability = 0,
        uvIndex = 0.0,
    )
    YoullBeColdTheme {
        RecommendationCard(weatherWithRecommendation = WeatherWithRecommendation(
            weather = persistentListOf(weather, weather, weather),
            recommendation = Recommendation(
                UvRecommendation.HighProtection,
                RainRecommendation.HeavyRain,
                persistentListOf(Clothes.SHORT_SLEEVE, Clothes.TENNIS_SHOES),
                Certainty.High
            )
        ))
    }
}
