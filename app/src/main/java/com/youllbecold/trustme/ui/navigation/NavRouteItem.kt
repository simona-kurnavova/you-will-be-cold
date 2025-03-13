package com.youllbecold.trustme.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.youllbecold.trustme.R
import com.youllbecold.trustme.common.ui.components.themed.IconType

/**
 * Wrapper class for NavRoute that provides additional information for the route.
 * - toolbar title/icon
 * - menu title/icon
 * - floating action title/icon
 */
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
        override val showInfoAction: Boolean = true

        override val menuTitle: Int = R.string.menu_home
        override val menuIcon: ImageVector = Icons.Filled.Home
        override val position: Int = 0

        override val floatingActionTitle: Int = R.string.add_log_action
        override val floatingActionIcon: ImageVector = Icons.Filled.Add
        override val floatingActionTo: NavRoute = NavRoute.AddLog
    }

    data object RecommendationItem : NavRouteItem(
        navRoute = NavRoute.Recommendation
    ), MenuItem, Toolbar {
        override val toolbarTitle: Int = R.string.toolbar_title_recommendation
        override val showInfoAction: Boolean = true

        override val menuTitle: Int = R.string.menu_recommendation
        override val menuIcon: ImageVector = Icons.Filled.FavoriteBorder
        override val position: Int = 1
    }

    data object HistoryItem : NavRouteItem(
        navRoute = NavRoute.History
    ), MenuItem, FloatingAction, Toolbar {
        override val toolbarTitle: Int = R.string.toolbar_title_log_history
        override val toolbarIcon: IconType? = IconType.Book
        override val showInfoAction: Boolean = true

        override val menuTitle: Int = R.string.menu_history
        override val menuIcon: ImageVector = Icons.AutoMirrored.Filled.List
        override val position: Int = 2

        override val floatingActionTitle: Int = R.string.add_log_action
        override val floatingActionIcon: ImageVector = Icons.Filled.Add
        override val floatingActionTo: NavRoute = NavRoute.AddLog
    }

    data object SettingsItem : NavRouteItem(
        navRoute = NavRoute.Settings
    ), MenuItem, Toolbar {
        override val toolbarTitle: Int = R.string.toolbar_title_settings
        override val toolbarIcon: IconType? = IconType.Tool
        override val showInfoAction: Boolean = true

        override val menuTitle: Int = R.string.menu_settings
        override val menuIcon: ImageVector = Icons.Filled.Settings
        override val position: Int = 3
    }

    data object AddLogItem : NavRouteItem(
        navRoute = NavRoute.AddLog
    ), Toolbar {
        override val toolbarTitle: Int = R.string.toolbar_title_add_log
        override val showBackButton: Boolean = true
    }

    data object EditLogItem : NavRouteItem(
        navRoute = NavRoute.EditLog
    ), Toolbar {
        override val toolbarTitle: Int = R.string.toolbar_title_edit_log
        override val showBackButton: Boolean = true
    }
    data object WelcomeItem : NavRouteItem(
        navRoute = NavRoute.Welcome
    )

    data object LocationPermissionItem : NavRouteItem(
        navRoute = NavRoute.LocationPermission
    )

    data object InfoItem : NavRouteItem(
        navRoute = NavRoute.Info
    ), Toolbar {
        override val toolbarTitle: Int = R.string.toolbar_title_info
        override val toolbarIcon: IconType = IconType.Thermometer
        override val showBackButton: Boolean = true
    }

    data object RecommendNotificationPermItem : NavRouteItem(
        navRoute = NavRoute.RecommendNotificationPerm
    )

    data object LogRemindNotificationPermItem : NavRouteItem(
        navRoute = NavRoute.LogRemindNotificationPerm
    )

    fun isMenuItem(): Boolean = this is MenuItem

    fun getToolbar(): Toolbar? = this as? Toolbar

    fun getFloatingAction(): FloatingAction? = this as? FloatingAction

    companion object {
        /**
         * Returns all NavRouteItems.
         */
        fun allNavRouteItems(): List<NavRouteItem> =
            NavRouteItem::class.sealedSubclasses.map {
                it.objectInstance ?: throw IllegalArgumentException("Object instance not found")
            }

        /**
         * Returns NavRouteItem for the given route.
         */
        fun fromRoute(route: String): NavRouteItem {
            allNavRouteItems().forEach {
                val routeWithoutArgs = route.split("/").first()
                if (it.navRoute.baseRoot == routeWithoutArgs) return it
            }
            throw IllegalArgumentException("Route not found: $route")
        }

        /**
         * Returns menu items sorted by position.
         */
        fun getMenuItems(): List<MenuItem> = allNavRouteItems()
            .filterIsInstance<MenuItem>()
            .sortedBy { it.position }
    }
}
