package com.youllbecold.trustme.ui.components.generic.attributes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
data class IconAttr(
    val size: Dp,
    val contentDescription: String? = null
) {
    companion object {
        const val SMALL_ICON_SIZE = 24
        const val BIG_ICON_SIZE = 48
    }
}

@Composable
fun defaultSmallIconAttr(): IconAttr = IconAttr(
    size = IconAttr.SMALL_ICON_SIZE.dp,
)

@Composable
fun defaultBigIconAttr(): IconAttr = IconAttr(
    size = IconAttr.BIG_ICON_SIZE.dp,
)
