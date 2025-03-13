package com.youllbecold.trustme.common.ui.model.clothes

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import com.youllbecold.trustme.common.ui.components.themed.IconType

/**
 * Represents one piece of clothing, used as clothing representation in UI.
 *
 * @param id The unique identifier of the clothes.
 * @param name The name of the clothes.
 * @param category The category of the clothes.
 * @param icon The icon for the clothes.
 */
@Stable
data class Clothes(
    val id: Int,
    @StringRes
    val name: Int,
    val category: ClothesCategory,
    val icon: IconType
)

