package com.youllbecold.trustme.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents navigation routes/destinations.
 *
 * @property path Route path.
 * @property icon Icon for the route (shown in bottom menu).
 */
sealed class NavRoute(val path: String, val icon: ImageVector) {

    /**
     * Home screen - default screen.
     */
    data object Home: NavRoute(HOME_PATH, icon = Icons.Filled.Home)

    /**
     * History screen.
     */
    data object History: NavRoute(HISTORY_PATH, icon = Icons.AutoMirrored.Filled.List)

    /**
     * Settings/Preferences screen.
     */
    data object Settings: NavRoute(SETTINGS_PATH, icon = Icons.Filled.Settings)
}

/**
 * Path constants.
 */
private const val HOME_PATH = "home"
private const val HISTORY_PATH ="history"
private const val SETTINGS_PATH ="settings"

/**
 * List of all bottom navigation items.
 */
internal val bottomNavItems = listOf(
    NavRoute.Home,
    NavRoute.History,
    NavRoute.Settings
)
