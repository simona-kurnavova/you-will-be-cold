package com.youllbecold.trustme.ui.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

/**
 * Bottom navigation bar.
 */
@Composable
fun NavigationBar(navController: NavController) {
    androidx.compose.material3.NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        bottomNavItems.forEach { BottomNavItem(it, navController, currentDestination) }
    }
}

/**
 * Composable representing one item in bottom navigation.
 */
@Composable
private fun RowScope.BottomNavItem(
    item: NavRoute,
    navController: NavController,
    currentDestination: NavDestination?
) {
    NavigationBarItem(
        icon = { Icon(item.icon, contentDescription = item.path) },
        label = { Text(item.path) },
        selected = currentDestination?.hierarchy?.any { it.route == item.path } == true,
        onClick = {
            navController.navigate(item.path) {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
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
