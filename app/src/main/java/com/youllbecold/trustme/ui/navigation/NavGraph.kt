package com.youllbecold.trustme.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.youllbecold.trustme.ui.screens.AddLogRoot
import com.youllbecold.trustme.ui.screens.HistoryScreenRoot
import com.youllbecold.trustme.ui.screens.HomeScreenRoot
import com.youllbecold.trustme.ui.screens.SettingsScreenRoot
import com.youllbecold.trustme.ui.screens.LocationPermissionRoot
import com.youllbecold.trustme.ui.screens.WelcomeScreenRoot
import com.youllbecold.trustme.ui.utils.popAllAndNavigate

/**
 * Navigation graph for the application.
 */
@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = NavRoute.Home.route, modifier = modifier) {
        composable(NavRoute.Home.route) { HomeScreenRoot() }
        composable(NavRoute.History.route) { HistoryScreenRoot() }
        composable(NavRoute.Settings.route) { SettingsScreenRoot() }
        composable(NavRoute.AddLog.route) {
            AddLogRoot(
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(NavRoute.Welcome.route) {
            WelcomeScreenRoot(
                onNavigateToDashboard = { navController.popAllAndNavigate(NavRoute.Home.route) },
                onNavigateToLocation = { navController.popAllAndNavigate(NavRoute.LocationPermission.route) }
            )
        }
        composable(NavRoute.LocationPermission.route) {
            LocationPermissionRoot { navController.popAllAndNavigate(NavRoute.Home.route) }
        }
    }
}
