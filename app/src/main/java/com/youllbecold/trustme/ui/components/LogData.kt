package com.youllbecold.trustme.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.IconType
import com.youllbecold.trustme.ui.components.generic.SelectableItemContent
import com.youllbecold.trustme.ui.viewmodels.FeelingState

fun Clothes.Category.getUiData(): Pair<String, IconType> =
    when (this) {
        Clothes.Category.TOPS -> "Tops" to IconType.TShirt
        Clothes.Category.JACKETS -> "Jackets" to IconType.Jacket
        Clothes.Category.BOTTOMS -> "Bottoms" to IconType.Pants
        Clothes.Category.SHOES -> "Shoes" to IconType.Shoes
        Clothes.Category.HATS -> "Hats" to  IconType.Hat
        Clothes.Category.ACCESSORIES -> "Accessories" to IconType.Sunglasses
        Clothes.Category.FULL_BODY -> "Dresses and overalls" to IconType.Dress
    }

fun Clothes.Category.getItems(): List<SelectableItemContent> {
    return Clothes.entries.filter { it.category == this }.map {
        SelectableItemContent(
            iconType = IconType.TShirt,
            title = it.name
        )
    }
}

@Composable
fun selectableFeelings(): List<SelectableItemContent> =
    FeelingState.entries.map { feeling ->
        when (feeling) {
            FeelingState.VERY_WARM -> SelectableItemContent(
                IconType.Fire,
                stringResource(id = R.string.feeling_very_hot)
            )

            FeelingState.WARM -> SelectableItemContent(
                IconType.Sun,
                stringResource(id = R.string.feeling_hot)
            )

            FeelingState.NORMAL -> SelectableItemContent(
                IconType.SmileEmoji,
                stringResource(id = R.string.feeling_normal)
            )

            FeelingState.COLD -> SelectableItemContent(
                IconType.Snowflake,
                stringResource(id = R.string.feeling_cold)
            )

            FeelingState.VERY_COLD -> SelectableItemContent(
                IconType.Popsicle,
                stringResource(id = R.string.feeling_very_cold)
            )
        }
    }