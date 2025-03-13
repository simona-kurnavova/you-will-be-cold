package com.youllbecold.trustme.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Interface to by implemented by menu items.
 */
interface MenuItem {
    /**
     * Menu title.
     */
    val menuTitle: Int
    /**
     * Menu icon.
     */
    val menuIcon: ImageVector
    /**
     * Menu position.
     */
    val position: Int
}
