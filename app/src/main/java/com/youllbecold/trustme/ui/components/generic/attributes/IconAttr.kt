package com.youllbecold.trustme.ui.components.generic.attributes

import androidx.compose.runtime.Composable

data class IconAttr(
    val size: Int,
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
)

@Composable
fun defaultBigIconAttr(): IconAttr = IconAttr(
    size = IconAttr.BIG_ICON_SIZE,
)
