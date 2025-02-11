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
                navigateToHistory = { navController.navigate(NavRoute.History.route) }
            )
        }
    }
}
