package com.youllbecold.trustme.common.ui.components.utils

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource

/**
 * Creates Painter for vector image.
 *
 * @param image - drawable resource id.
 */
@Composable
fun rememberVector(@DrawableRes image: Int): Painter =
    rememberVectorPainter(ImageVector.vectorResource(image))
