package com.youllbecold.trustme.ui.components.generic.attributes

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class IconAttr(
    val size: Int,
    val tint: Color,
    val contentDescription: String? = null
) {
    companion object {
        const val SMALL_ICON_SIZE = 24
        const val BIG_ICON_SIZE = 48
    }
}

@Composable
fun defaultSmallIconAttr(): IconAttr = IconAttr(
    size = IconAttr.SMALL_ICON_SIZE,
    tint =  MaterialTheme.colorScheme.primary
)