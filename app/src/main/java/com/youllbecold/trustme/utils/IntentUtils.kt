package com.youllbecold.trustme.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

/**
 * Utility class for common Intent operations.
 */
object IntentUtils {
    /**
     * Open the app settings screen.
     *
     * @param context The context to use.
     */
    fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent)
    }
}
