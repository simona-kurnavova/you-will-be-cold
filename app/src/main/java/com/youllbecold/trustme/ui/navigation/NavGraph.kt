package com.youllbecold.trustme.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.youllbecold.trustme.ui.screens.HistoryScreen
import com.youllbecold.trustme.ui.screens.HomeScreen
import com.youllbecold.trustme.ui.screens.SettingsScreen

/**
 * Navigation graph for the application.
 */
@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = NavRoute.Home.path, modifier = modifier) {
        composable(route = NavRoute.Home.path) { HomeScreen() }
        composable(route = NavRoute.History.path) { HistoryScreen() }
        composable(route = NavRoute.Settings.path) { SettingsScreen() }
    }
}
