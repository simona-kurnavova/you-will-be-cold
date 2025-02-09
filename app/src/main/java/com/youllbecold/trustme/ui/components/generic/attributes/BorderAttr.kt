package com.youllbecold.trustme.ui.components.generic.attributes

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Attributes for setting up borders for Composables.
 */
data class BorderAttr(
    val width: Double,
    val color: Color,
)

@Composable
fun defaultBorderAttr(): BorderAttr = BorderAttr(
    color = MaterialTheme.colorScheme.onBackground,
    width = 0.1
)

@Composable
fun fadedBorderAttr(): BorderAttr = defaultBorderAttr().let {
    it.copy(color = it.color.copy(alpha = 0.5f))
}
