package com.youllbecold.trustme.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.youllbecold.trustme.R
import com.youllbecold.trustme.ui.components.generic.IconType

/**
 * Represents navigation routes/destinations.
 */
sealed class NavRoute(val route: String) {

    /**
     * Home screen - default screen.
     */
    data object Home : NavRoute("home")

    /**
     * History screen.
     */
    data object History : NavRoute("history")

    /**
     * Settings/Preferences screen.
     */
    data object Settings : NavRoute("settings")

    /**
     * Add log screen.
     */
    data object AddLog : NavRoute("add_log")

    /**
     * Welcome screen.
     */
    data object Welcome : NavRoute("welcome")

    /**
     * Location permission screen.
     */
    data object LocationPermission : NavRoute("location_permission")
}

sealed class NavRouteItem(val navRoute: NavRoute) {
    data object HomeItem : NavRouteItem(
        navRoute = NavRoute.Home
    ), MenuItem, FloatingAction, Toolbar {
        override val toolbarTitle: Int = R.string.toolbar_title_home
        override val toolbarIcon: IconType?
            get() = listOf(
                IconType.Cloud,
                IconType.Popsicle,
                IconType.Fog,
                IconType.Wind,
                IconType.Snowflake
            ).random()

        override val menuTitle: Int = R.string.menu_home
        override val menuIcon: ImageVector = Icons.Filled.Home

        override val floatingActionTitle: Int = R.string.add_log_action
        override val floatingActionIcon: ImageVector = Icons.Filled.Add
        override val floatingActionTo: NavRoute = NavRoute.AddLog
    }

    data object HistoryItem : NavRouteItem(
        navRoute = NavRoute.History
    ), MenuItem, FloatingAction, Toolbar {
        override val toolbarTitle: Int = R.string.toolbar_title_log_history
        override val toolbarIcon: IconType? = IconType.Book

        override val menuTitle: Int = R.string.menu_history
        override val menuIcon: ImageVector = Icons.AutoMirrored.Filled.List

        override val floatingActionTitle: Int = R.string.add_log_action
        override val floatingActionIcon: ImageVector = Icons.Filled.Add
        override val floatingActionTo: NavRoute = NavRoute.AddLog
    }

    data object SettingsItem : NavRouteItem(
        navRoute = NavRoute.Settings
    ), MenuItem, Toolbar {
        override val toolbarTitle: Int = R.string.toolbar_title_settings
        override val toolbarIcon: IconType? = IconType.Tool

        override val menuTitle: Int = R.string.menu_settings
        override val menuIcon: ImageVector = Icons.Filled.Settings
    }

    data object AddLogItem : NavRouteItem(
        navRoute = NavRoute.AddLog
    ), Toolbar {
        override val toolbarTitle: Int = R.string.toolbar_title_add_log
    }

    data object WelcomeItem : NavRouteItem(
        navRoute = NavRoute.Welcome
    )

    data object LocationPermissionItem : NavRouteItem(
        navRoute = NavRoute.LocationPermission
    )

    fun isMenuItem(): Boolean = this is MenuItem

    fun getToolbar(): Toolbar? = this as? Toolbar

    fun getFloatingAction(): FloatingAction? = this as? FloatingAction

    companion object {
        fun fromRoute(route: String): NavRouteItem = when (route) {
            NavRoute.Home.route -> HomeItem
            NavRoute.History.route -> HistoryItem
            NavRoute.Settings.route -> SettingsItem
            NavRoute.AddLog.route -> AddLogItem
            NavRoute.Welcome.route -> WelcomeItem
            NavRoute.LocationPermission.route -> LocationPermissionItem
            else -> throw IllegalArgumentException("Route $route not found")
        }

        fun allNavRouteItems(): List<NavRouteItem> = listOf(
            HomeItem,
            HistoryItem,
            SettingsItem,
            AddLogItem,
            WelcomeItem,
            LocationPermissionItem
        )
    }
}

interface MenuItem {
    val menuTitle: Int
    val menuIcon: ImageVector
}

interface FloatingAction {
    val floatingActionTitle: Int
    val floatingActionIcon: ImageVector
    val floatingActionTo: NavRoute
}

interface Toolbar {
    @get:StringRes
    val toolbarTitle: Int
    val toolbarIcon: IconType?
        get() = null
    val toolbarMenu: List<ToolbarMenuItem>
        get() = emptyList()
}

data class ToolbarMenuItem(
    val title: Int,
    val icon: IconType?,
    val actionTo: NavRoute
)
