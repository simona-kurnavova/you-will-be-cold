package com.youllbecold.trustme.common.ui.components.utils

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

/**
 * Creates Painter for vector image.
 *
 * @param image - drawable resource id.
 */
@Composable
fun rememberVector(@DrawableRes image: Int): Painter =
    rememberVectorPainter(ImageVector.vectorResource(image))


/**
 * Creates TextStyle with center alignment.
 */
fun TextStyle.center() = this.copy(textAlign = TextAlign.Center)
