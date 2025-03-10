package com.youllbecold.trustme.common.ui.components.cards

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.recomendation.model.Certainty
import com.youllbecold.recomendation.model.RainRecommendation
import com.youllbecold.recomendation.model.UvRecommendation
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.attributes.copyWithAlpha
import com.youllbecold.trustme.common.ui.attributes.defaultMediumTextAttr
import com.youllbecold.trustme.common.ui.components.icontext.IconText
import com.youllbecold.trustme.common.ui.components.themed.IconType
import com.youllbecold.trustme.common.ui.components.themed.ThemedCard
import com.youllbecold.trustme.common.ui.components.themed.ThemedHorizontalDivider
import com.youllbecold.trustme.common.ui.components.themed.ThemedText
import com.youllbecold.trustme.common.ui.model.log.mappers.clothesName
import com.youllbecold.trustme.common.ui.model.log.mappers.icon
import com.youllbecold.trustme.common.ui.model.recommendation.Recommendation
import com.youllbecold.trustme.common.ui.model.recommendation.mappers.getTitle
import com.youllbecold.trustme.common.ui.model.weather.mappers.feelLikeDescription
import com.youllbecold.trustme.common.ui.model.weather.mappers.getTitle
import com.youllbecold.trustme.common.ui.model.weather.mappers.temperatureRangeDescription
import com.youllbecold.trustme.common.ui.model.weatherwithrecommend.WeatherWithRecommendation
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme
import com.youllbecold.weather.model.Weather
import com.youllbecold.weather.model.WeatherEvaluation
import kotlinx.collections.immutable.persistentListOf

@Composable
fun RecommendationCard(
    weatherWithRecommendation: WeatherWithRecommendation,
    modifier: Modifier = Modifier
) {
    ThemedCard(modifier = modifier) {
        Column {
            val recommendation = weatherWithRecommendation.recommendation

            if (recommendation == null) {
                ThemedText(
                    text = stringResource(R.string.recom_no_recommendation),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                return@Column
            }

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

                ThemedHorizontalDivider(
                    modifier = Modifier.padding(vertical = ITEMS_PADDING.dp)
                )
            }

            ClothesRecommendations(recommendation)

            ThemedHorizontalDivider(
                modifier = Modifier.padding(vertical = ITEMS_PADDING.dp)
            )

            ThemedText(
                text = stringResource(R.string.recom_certainty_description, recommendation.certainty.getTitle()),
                textAttr = defaultMediumTextAttr().copyWithAlpha(TEXT_ALPHA),
            )
        }
    }
}

private const val TEXT_ALPHA = 0.6f

@Composable
private fun ClothesRecommendations(
    recommendation: Recommendation,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ThemedText(stringResource(R.string.recom_clothes_title))

        Spacer(modifier = Modifier.height(ITEMS_PADDING.dp))

        Column{
            recommendation.clothes.forEach {
                IconText(
                    text = it.clothesName(),
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
        time = 1000,
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
