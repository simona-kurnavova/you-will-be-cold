package com.youllbecold.trustme.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.youllbecold.trustme.ui.navigation.utils.popAllAndNavigate
import com.youllbecold.trustme.log.add.ui.AddLogRoot
import com.youllbecold.trustme.log.edit.ui.EditLogRoot
import com.youllbecold.trustme.log.history.ui.HistoryScreenRoot
import com.youllbecold.trustme.recommend.home.ui.HomeScreenRoot
import com.youllbecold.trustme.info.ui.InfoScreen
import com.youllbecold.trustme.settings.ui.SettingsScreenRoot
import com.youllbecold.trustme.overlays.locationpermission.ui.LocationPermissionRoot
import com.youllbecold.trustme.overlays.notificationpermissions.ui.NotificationScreenRoot
import com.youllbecold.trustme.overlays.notificationpermissions.ui.NotificationType
import com.youllbecold.trustme.recommend.ranged.ui.RecommendScreenRoot
import com.youllbecold.trustme.overlays.welcome.ui.WelcomeScreenRoot

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
