package com.youllbecold.trustme.ui.components.generic.attributes

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Stable
data class TextAttr(
    val style: TextStyle,
    val color: Color
)

@Composable
fun defaultMediumTextAttr(): TextAttr = TextAttr(
    style = MaterialTheme.typography.bodyMedium,
    color = MaterialTheme.colorScheme.onBackground
)

@Composable
fun defaultLargeTextAttr(): TextAttr = TextAttr(
    style = MaterialTheme.typography.bodyLarge,
    color = MaterialTheme.colorScheme.onBackground
)

@Composable
fun defaultSmallTextAttr(): TextAttr = TextAttr(
    style = MaterialTheme.typography.bodySmall,
    color = MaterialTheme.colorScheme.onBackground
)

@Composable
fun defaultSmallFadedTextAttr(): TextAttr = TextAttr(
    style = MaterialTheme.typography.bodySmall,
    color = MaterialTheme.colorScheme.onBackground.copy(alpha = FADED_ALPHA)
)

/**
 * Returns a copy of this [TextAttr] with the color set to the given [alpha].
 */
fun TextAttr.copyWithAlpha(alpha: Float): TextAttr = copy(color = color.copy(alpha = alpha))

private const val FADED_ALPHA = 0.7f
