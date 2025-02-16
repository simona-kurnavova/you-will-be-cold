package com.youllbecold.trustme.ui.utils

import com.youllbecold.logdatabase.model.Clothes
import com.youllbecold.trustme.ui.components.generic.IconType
import com.youllbecold.trustme.ui.components.generic.SelectableItemContent

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
val Clothes.Category.title: String
    get() = when (this) {
        Clothes.Category.TOPS -> "Tops"
        Clothes.Category.JACKETS -> "Jackets"
        Clothes.Category.BOTTOMS -> "Pants"
        Clothes.Category.SHOES -> "Shoes"
        Clothes.Category.HATS -> "Hats"
        Clothes.Category.ACCESSORIES -> "Accessories"
        Clothes.Category.FULL_BODY -> "Dresses and overalls"
    }

/**
 * Gets the [IconType] for [Clothes].
 */
val Clothes.icon: IconType
    get() = this.category.icon

/**
 * Get the title of the [Clothes].
 */
val Clothes.title: String
    get() = when(this) {
        Clothes.SHORT_SLEEVE -> "Short sleeve"
        Clothes.LONG_SLEEVE -> "Long sleeve"
        Clothes.SHORT_SKIRT -> "Short skirt"
        Clothes.SHORTS -> "Shorts"
        Clothes.JEANS -> "Jeans"
        Clothes.SANDALS -> "Sandals"
        Clothes.TENNIS_SHOES -> "Tennis shoes"
        Clothes.DRESS -> "Dress"
    }

/**
 * Converts a set of [Clothes] to a list of [SelectableItemContent].
 */
fun List<Clothes>.toSelectableItemContent(): List<SelectableItemContent> =
    this.map { item ->
        SelectableItemContent(
            title = item.title,
            iconType = item.icon,
        )
    }

/**
 * Gets the list of [Clothes] for the [Clothes.Category].
 */
val Clothes.Category.items: List<Clothes>
    get() = Clothes.entries.filter { it.category == this }

fun Set<Clothes>.withCategory(category: Clothes.Category): List<Clothes> =
    this.filter { it.category == category }