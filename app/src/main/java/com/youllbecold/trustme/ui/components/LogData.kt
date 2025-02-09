package com.youllbecold.trustme.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
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

fun ClothesCategory.getUiData(): Pair<String, Int> =
    when (this) {
        ClothesCategory.Top -> "Tops" to R.drawable.ic_shirt
        ClothesCategory.Jackets -> "Jackets" to R.drawable.ic_jacket
        ClothesCategory.Bottom -> "Bottoms" to R.drawable.ic_pants
        ClothesCategory.Shoes -> "Shoes" to R.drawable.ic_stocking
        ClothesCategory.Hats -> "Hats" to R.drawable.ic_hat
        ClothesCategory.Accessories -> "Accessories" to R.drawable.ic_sunglasses
        ClothesCategory.FullBody -> "Dresses and overalls" to R.drawable.ic_dress
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