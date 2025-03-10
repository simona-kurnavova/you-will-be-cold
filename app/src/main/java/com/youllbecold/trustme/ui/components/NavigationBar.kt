package com.youllbecold.trustme.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.youllbecold.trustme.ui.navigation.MenuItem
import com.youllbecold.trustme.ui.navigation.NavRoute
import com.youllbecold.trustme.ui.navigation.NavRouteItem

/**
 * Bottom navigation bar.
 */
@Composable
fun NavigationBar(navController: NavController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        NavRouteItem.Companion.getMenuItems()
            .forEach {
                BottomNavItem((it as NavRouteItem).navRoute, it, navController, currentDestination)
            }
    }
}

/**
 * Composable representing one item in bottom navigation.
 */
@Composable
private fun RowScope.BottomNavItem(
    navRoute: NavRoute,
    menuItem: MenuItem,
    navController: NavController,
    currentDestination: NavDestination?
)  {
    NavigationBarItem(
        icon = { Icon(imageVector = menuItem.menuIcon, contentDescription = null) },
        label = { Text(stringResource(menuItem.menuTitle)) },
        selected = currentDestination?.hierarchy?.any { it.route == navRoute.route } == true,
        onClick = {
            navController.navigate(navRoute.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }
        }
    )
}

@Preview
@Composable
private fun NavigationBarPreview(navController: NavController = rememberNavController()) {
    NavigationBar(navController)
}
