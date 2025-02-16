package com.youllbecold.trustme.ui.utils

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.IconType
import com.youllbecold.trustme.ui.components.generic.SelectableItemContent
import com.youllbecold.trustme.ui.viewmodels.FeelingState
import com.youllbecold.trustme.ui.viewmodels.FeelingsState

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
