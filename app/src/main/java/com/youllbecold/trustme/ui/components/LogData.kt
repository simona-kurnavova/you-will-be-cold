package com.youllbecold.trustme.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.SelectableItemContent
import com.youllbecold.trustme.ui.viewmodels.FeelingState
import java.time.LocalTime

fun Clothes.Category.getUiData(): Pair<String, Int> =
    when (this) {
        Clothes.Category.TOPS -> "Tops" to R.drawable.ic_shirt
        Clothes.Category.JACKETS -> "Jackets" to R.drawable.ic_jacket
        Clothes.Category.BOTTOMS -> "Bottoms" to R.drawable.ic_pants
        Clothes.Category.SHOES -> "Shoes" to R.drawable.ic_stocking
        Clothes.Category.HATS -> "Hats" to R.drawable.ic_hat
        Clothes.Category.ACCESSORIES -> "Accessories" to R.drawable.ic_sunglasses
        Clothes.Category.FULL_BODY -> "Dresses and overalls" to R.drawable.ic_dress
    }

fun Clothes.Category.getItems(): List<SelectableItemContent> {
    return Clothes.entries.filter { it.category == this }.map {
        SelectableItemContent(
            icon = R.drawable.ic_shirt,
            title = it.name
        )
    }
}

@Composable
fun selectableFeelings(): List<SelectableItemContent> =
    FeelingState.entries.map { feeling ->
        when (feeling) {
            FeelingState.VERY_WARM -> SelectableItemContent(
                R.drawable.ic_fire,
                stringResource(id = R.string.feeling_very_hot)
            )

            FeelingState.WARM -> SelectableItemContent(
                R.drawable.ic_sun,
                stringResource(id = R.string.feeling_hot)
            )

            FeelingState.NORMAL -> SelectableItemContent(
                R.drawable.ic_smile,
                stringResource(id = R.string.feeling_normal)
            )

            FeelingState.COLD -> SelectableItemContent(
                R.drawable.ic_snowflake,
                stringResource(id = R.string.feeling_cold)
            )

            FeelingState.VERY_COLD -> SelectableItemContent(
                R.drawable.ic_popsicle,
                stringResource(id = R.string.feeling_very_cold)
            )
        }
    }