package com.youllbecold.trustme.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.IconType
import com.youllbecold.trustme.ui.components.generic.inputs.SelectableItemContent

/**
 * Gets the [IconType] for [Clothes.Category].
 */
val Clothes.Category.icon: IconType
   get() = when (this) {
        Clothes.Category.TOPS -> IconType.TShirt
        Clothes.Category.JACKETS -> IconType.Jacket
        Clothes.Category.BOTTOMS -> IconType.Pants
        Clothes.Category.SHOES -> IconType.Shoes
        Clothes.Category.HATS -> IconType.Hat
        Clothes.Category.ACCESSORIES -> IconType.Sunglasses
        Clothes.Category.FULL_BODY -> IconType.Dress
    }

/**
 * Get the title of the [Clothes.Category].
 */
@Composable
fun Clothes.Category.getTitle(): String = when (this) {
        Clothes.Category.TOPS -> stringResource(R.string.clothes_category_tops)
        Clothes.Category.JACKETS -> stringResource(R.string.clothes_category_jackets)
        Clothes.Category.BOTTOMS -> stringResource(R.string.clothes_category_pants)
        Clothes.Category.SHOES -> stringResource(R.string.clothes_category_shoes)
        Clothes.Category.HATS -> stringResource(R.string.clothes_category_hats)
        Clothes.Category.ACCESSORIES -> stringResource(R.string.clothes_category_accessories)
        Clothes.Category.FULL_BODY -> stringResource(R.string.clothes_category_dresses_and_overalls)
    }

/**
 * Gets the [IconType] for [Clothes].
 */
val Clothes.icon: IconType
    get() = this.category.icon

/**
 * Get the title of the [Clothes].
 */
@Composable
fun Clothes.getTitle(): String = when (this) {
    Clothes.SHORT_SLEEVE -> stringResource(R.string.clothes_short_sleeve)
    Clothes.LONG_SLEEVE -> stringResource(R.string.clothes_long_sleeve)
    Clothes.SHORT_SKIRT -> stringResource(R.string.clothes_short_skirt)
    Clothes.SHORTS -> stringResource(R.string.clothes_shorts)
    Clothes.JEANS -> stringResource(R.string.clothes_jeans)
    Clothes.SANDALS -> stringResource(R.string.clothes_sandals)
    Clothes.TENNIS_SHOES -> stringResource(R.string.clothes_tennis_shoes)
    Clothes.DRESS -> stringResource(R.string.clothes_dress)
}

/**
 * Converts a set of [Clothes] to a list of [SelectableItemContent].
 */
@Composable
fun List<Clothes>.toSelectableItemContent(): List<SelectableItemContent> =
    this.map { item ->
        SelectableItemContent(
            title = item.getTitle(),
            iconType = item.icon,
        )
    }

/**
 * Gets the list of [Clothes] for the [Clothes.Category].
 */
val Clothes.Category.items: List<Clothes>
    get() = Clothes.entries.filter { it.category == this }

/**
 * Filters the list of [Clothes] by the [Clothes.Category].
 */
fun Set<Clothes>.withCategory(category: Clothes.Category): List<Clothes> =
    this.filter { it.category == category }