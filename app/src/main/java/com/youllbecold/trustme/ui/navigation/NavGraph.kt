package com.youllbecold.trustme.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.youllbecold.trustme.ui.screens.AddLogRoot
import com.youllbecold.trustme.ui.screens.EditLogRoot
import com.youllbecold.trustme.ui.screens.HistoryScreenRoot
import com.youllbecold.trustme.ui.screens.HomeScreenRoot
import com.youllbecold.trustme.ui.screens.InfoScreen
import com.youllbecold.trustme.ui.screens.SettingsScreenRoot
import com.youllbecold.trustme.ui.screens.LocationPermissionRoot
import com.youllbecold.trustme.ui.screens.NotificationScreenRoot
import com.youllbecold.trustme.ui.screens.NotificationType
import com.youllbecold.trustme.ui.screens.RecommendScreenRoot
import com.youllbecold.trustme.ui.screens.WelcomeScreenRoot
import com.youllbecold.trustme.ui.utils.popAllAndNavigate

/**
 * Navigation graph for the application.
 */
@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = NavRoute.Home.route, modifier = modifier) {
        composable(NavRoute.Home.route) { HomeScreenRoot() }
        composable(NavRoute.Recommendation.route) { RecommendScreenRoot() }
        composable(NavRoute.History.route) {
            HistoryScreenRoot(
                navigateToEdit = { id ->
                    navController.navigate(NavRoute.EditLog.createRoute(id))
                }
            )
        }
        composable(NavRoute.Settings.route) {
            SettingsScreenRoot(
                navigateToLogRemindSetup = { navController.navigate(NavRoute.LogRemindNotificationPerm.route) },
                navigateToRecommendSetup = { navController.navigate(NavRoute.RecommendNotificationPerm.route) }
            )
        }
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

        composable(NavRoute.EditLog.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString(NavRoute.EditLog.ARG_ID)?.toIntOrNull() ?: 0
            EditLogRoot(
                id = id,
                navigateBack = { navController.popBackStack() },
            )
        }
        composable(NavRoute.Info.route) { InfoScreen() }
        composable(NavRoute.RecommendNotificationPerm.route) {
            NotificationScreenRoot(
                notification = NotificationType.RECOMMEND,
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(NavRoute.LogRemindNotificationPerm.route) {
            NotificationScreenRoot(
                notification = NotificationType.LOG_REMINDER,
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}
