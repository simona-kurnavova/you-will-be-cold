package com.youllbecold.trustme.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.SelectableItemContent
import java.time.LocalTime

data class LogData(
    val timeFrom: LocalTime,
    val timeTo: LocalTime,
    val overallFeeling: Feeling? = null,
    // TODO: Update
    val clothes: Map<ClothesCategory, List<String>> = emptyMap()
)

enum class Feeling {
    VeryHot,
    Hot,
    Normal,
    Cold,
    VeryCold,
}

enum class ClothesCategory {
    Top,
    Jackets,
    Bottom,
    Shoes,
    Hats,
    Accessories,
    FullBody
}

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


@Composable
fun feelingItems(): List<SelectableItemContent> =
    Feeling.entries.map { feeling ->
        when (feeling) {
            Feeling.VeryHot -> SelectableItemContent(
                R.drawable.ic_fire,
                stringResource(id = R.string.feeling_very_hot)
            )

            Feeling.Hot -> SelectableItemContent(
                R.drawable.ic_sun,
                stringResource(id = R.string.feeling_hot)
            )

            Feeling.Normal -> SelectableItemContent(
                R.drawable.ic_smile,
                stringResource(id = R.string.feeling_normal)
            )

            Feeling.Cold -> SelectableItemContent(
                R.drawable.ic_snowflake,
                stringResource(id = R.string.feeling_cold)
            )

            Feeling.VeryCold -> SelectableItemContent(
                R.drawable.ic_popsicle,
                stringResource(id = R.string.feeling_very_cold)
            )
        }
    }