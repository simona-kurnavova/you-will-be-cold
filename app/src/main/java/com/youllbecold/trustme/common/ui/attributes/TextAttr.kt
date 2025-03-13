package com.youllbecold.trustme.common.ui.attributes

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

/**
 * Attributes for setting up text for Composables.
 */
@Stable
data class TextAttr(
    val style: TextStyle,
    val color: Color,
    val textAlign: TextAlign = TextAlign.Start,
    val ellipsize: Boolean = false
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

@Composable
fun defaultMediumErrorTextAttr(): TextAttr = TextAttr(
    style = MaterialTheme.typography.bodyMedium,
    color = MaterialTheme.colorScheme.error,
    textAlign = TextAlign.Center
)

/**
 * Returns a copy of this [TextAttr] with the color set to the given [alpha].
 */
fun TextAttr.withAlpha(alpha: Float): TextAttr = copy(color = color.copy(alpha = alpha))

/**
 * Returns a copy of this [TextAttr] with the text alignment set to center.
 */
fun TextAttr.centered(): TextAttr = copy(textAlign = TextAlign.Center)

/**
 * Returns a copy of this [TextAttr] with the elipsize set to true.
 */
fun TextAttr.ellipsized(): TextAttr = copy(ellipsize = true)

private const val FADED_ALPHA = 0.7f
