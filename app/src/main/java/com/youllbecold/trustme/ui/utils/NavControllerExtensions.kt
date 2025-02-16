package com.youllbecold.trustme.ui.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * Navigates to the route and pops all the previous routes from the back stack.
 */
fun NavController.popAllAndNavigate(route: String) {
    navigate(route) {
        popUpTo(0) { inclusive = true }
    }
}

/**
 * Returns the current route of the NavController.
 */
@Composable
fun NavController.currentRoute(): String? = currentBackStackEntryAsState().value?.destination?.route
