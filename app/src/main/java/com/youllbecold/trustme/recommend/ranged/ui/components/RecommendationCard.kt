package com.youllbecold.trustme.recommend.ranged.ui.components

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
import kotlinx.collections.immutable.PersistentList

@Composable
fun RecommendationCard(
    temperatureRangeDescription: String,
    feelsLikeDescription: String,
    certaintyLevelDescription: String,
    uvWarning: String?,
    rainWarning: String?,
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

            listOfNotNull(
                temperatureRangeDescription to IconType.Thermometer,
                feelsLikeDescription to IconType.Person,
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

            ThemedText(
                text = stringResource(R.string.recom_certainty_description, certaintyLevelDescription),
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

private const val ITEMS_PADDING = 8

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun RecommendationCardPreview() {
    YoullBeColdTheme {
        RecommendationCard(
            temperatureRangeDescription = "10°C - 15°C",
            feelsLikeDescription = "12°C",
            certaintyLevelDescription = "High",
            uvWarning = "High",
            rainWarning = "Low",
            clothes = ClothesCategory.getAll().first().getAllItems()
        )
    }
}
