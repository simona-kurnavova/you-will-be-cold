package com.youllbecold.trustme.ui.navigation

/**
 * Represents navigation routes/destinations.
 */
sealed class NavRoute(val baseRoot: String, val route: String = baseRoot) {

    /**
     * Home screen - default screen.
     */
    data object Home : NavRoute("home")

    /**
     * Recommendation screen.
     */
    data object Recommendation : NavRoute("recommendation")

    /**
     * History screen.
     */
    data object History : NavRoute("history")

    /**
     * Settings/Preferences screen.
     */
    data object Settings : NavRoute("settings")

    /**
     * Add log screen.
     */
    data object AddLog : NavRoute("add_log")

    /**
     * Edit log screen.
     */
    data object EditLog : NavRoute(baseRoot = "edit_log", route = "edit_log/{id}") {
        fun createRoute(id: Int) = "edit_log/$id"

        const val ARG_ID = "id"
    }

    /**
     * Welcome screen.
     */
    data object Welcome : NavRoute("welcome")

    /**
     * Location permission screen.
     */
    data object LocationPermission : NavRoute("location_permission")

    /**
     * Info screen.
     */
    data object Info : NavRoute("info")

    /**
     * Recommendation notification permission screen.
     */
    data object RecommendNotificationPerm : NavRoute("recommend_notification_perm")

    /**
     * Log remind notification permission screen.
     */
    data object LogRemindNotificationPerm : NavRoute("log_remind_notification_perm")
}
