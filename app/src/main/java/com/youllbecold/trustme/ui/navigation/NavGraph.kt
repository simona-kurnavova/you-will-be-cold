package com.youllbecold.trustme.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.youllbecold.trustme.ui.screens.HistoryScreen
import com.youllbecold.trustme.ui.screens.HomeScreen
import com.youllbecold.trustme.ui.screens.SettingsScreen
import com.youllbecold.trustme.ui.viewmodels.HomeViewModel
import com.youllbecold.trustme.ui.viewmodels.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Navigation graph for the application.
 */
@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = NavRoute.Home.path, modifier = modifier) {
        composable(route = NavRoute.Home.path) {
            val viewModel = koinViewModel<HomeViewModel>()
            HomeScreen(
                viewModel.locationGranted,
                viewModel.currentWeather
            )
        }
        composable(route = NavRoute.History.path) { HistoryScreen() }
        composable(route = NavRoute.Settings.path) {
            val viewmodel = koinViewModel<SettingsViewModel>()
            SettingsScreen(
                allowDailyNotification = viewmodel.allowDailyNotification,
                setAllowDailyNotification = viewmodel::setAllowDailyNotification
            )
        }
    }
}
