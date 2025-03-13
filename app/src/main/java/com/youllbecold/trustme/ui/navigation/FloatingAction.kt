package com.youllbecold.trustme.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Interface to be implemented by items that have floating action..
 */
interface FloatingAction {
    /**
     * Floating action title.
     */
    val floatingActionTitle: Int

    /**
     * Floating action icon.
     */
    val floatingActionIcon: ImageVector

    /**
     * Floating action navigation route.
     */
    val floatingActionTo: NavRoute
}
