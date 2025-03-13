package com.youllbecold.trustme.recommend.ranged.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.attributes.copyWithAlpha
import com.youllbecold.trustme.common.ui.attributes.defaultMediumTextAttr
import com.youllbecold.trustme.common.ui.components.icontext.IconText
import com.youllbecold.trustme.common.ui.components.themed.IconType
import com.youllbecold.trustme.common.ui.components.themed.ThemedCard
import com.youllbecold.trustme.common.ui.components.themed.ThemedHorizontalDivider
import com.youllbecold.trustme.common.ui.components.themed.ThemedText
import com.youllbecold.trustme.common.ui.mappers.getAllItems
import com.youllbecold.trustme.common.ui.model.clothes.Clothes
import com.youllbecold.trustme.common.ui.model.clothes.ClothesCategory
import com.youllbecold.trustme.common.ui.theme.YoullBeColdTheme
import com.youllbecold.trustme.common.ui.utils.TemperatureUiUtils
import com.youllbecold.trustme.recommend.ui.model.WeatherConditions
import com.youllbecold.trustme.recommend.ui.model.WeatherWithRecommendation
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun RecommendationCard(
    weather: WeatherWithRecommendation,
    clothes: PersistentList<Clothes>,
    modifier: Modifier = Modifier
) {
    ThemedCard(modifier = modifier) {
        Column {
            if (clothes.isEmpty()) {
                ThemedText(
                    text = stringResource(R.string.recom_no_recommendation),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                return@Column
            }

            val recommendation = weather.recommendationState
            val uvWarning = recommendation?.uvWarning?.let { stringResource(it) }
            val rainWarning = recommendation?.rainWarning?.let { stringResource(it) }

            listOfNotNull(
                weather.temperatureRangeDescription() to IconType.Thermometer,
                weather.feelLikeDescription() to IconType.Person,
                (uvWarning to IconType.Sun).takeIf {it.first != null },
                (rainWarning to IconType.Rain).takeIf { it.first != null }
            ).forEach { (text, iconType) ->
                IconText(
                    text = text ?: "",
                    iconType = iconType,
                )

                ThemedHorizontalDivider(
                    modifier = Modifier.padding(vertical = ITEMS_PADDING.dp)
                )
            }

            ClothesRecommendations(clothes)

            ThemedHorizontalDivider(
                modifier = Modifier.padding(vertical = ITEMS_PADDING.dp)
            )

            val certaintyText = stringResource(
                R.string.recom_certainty_description,
                recommendation?.certaintyLevel
                    ?.let { stringResource(it) }
                    ?: stringResource(R.string.unknown_label)
            )

            ThemedText(
                text = certaintyText,
                textAttr = defaultMediumTextAttr().copyWithAlpha(TEXT_ALPHA),
            )
        }
    }
}

private const val TEXT_ALPHA = 0.6f

@Composable
private fun ClothesRecommendations(
    clothes: PersistentList<Clothes>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ThemedText(stringResource(R.string.recom_clothes_title))

        Spacer(modifier = Modifier.height(ITEMS_PADDING.dp))

        Column{
            clothes.forEach {
                IconText(
                    text = stringResource(it.name),
                    iconType = it.icon,
                    modifier = Modifier.padding(vertical = ITEMS_PADDING.dp)
                )
            }
        }
    }
}

/**
 * Get description for the temperature range.
 */
@Composable
private fun WeatherWithRecommendation.temperatureRangeDescription(): String {
    return description(
        valueSelector = { it.temperature },
        singleResId = R.string.recom_description_single,
        rangeResId = R.string.recom_description_range
    )
}

/**
 * Get description for the apparent temperature range.
 */
@Composable
private fun WeatherWithRecommendation.feelLikeDescription(): String {
    return description(
        valueSelector = { it.apparentTemperature },
        singleResId = R.string.recom_apparent_description_single,
        rangeResId = R.string.recom_apparent_description
    )
}

@Composable
private fun WeatherWithRecommendation.description(
    valueSelector: (WeatherConditions) -> Double,
    singleResId: Int,
    rangeResId: Int
): String {
    val context = LocalContext.current
    val usesCelsius = weather.first().unitsCelsius

    val min = TemperatureUiUtils.getTemperatureString(context, weather.minOf(valueSelector), usesCelsius)
    val max = TemperatureUiUtils.getTemperatureString(context, weather.maxOf(valueSelector), usesCelsius)

    return if (weather.size == 1) {
        stringResource(singleResId, min)
    } else {
        stringResource(rangeResId, min, max)
    }
}

private const val ITEMS_PADDING = 8

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun RecommendationCardPreview() {
    YoullBeColdTheme {
        RecommendationCard(
            weather = WeatherWithRecommendation(
                weather = persistentListOf(
                    WeatherConditions(
                        time = 0,
                        unitsCelsius = true,
                        temperature = 10.0,
                        apparentTemperature = 12.0,
                        icon = IconType.Sun,
                        relativeHumidity = 50,
                        windSpeed = 10.0,
                        precipitationProbability = 0,
                        uvIndex = 5.0
                    )
                ),
                recommendationState = null
            ),
            clothes = ClothesCategory.getAll().first().getAllItems()
        )
    }
}
