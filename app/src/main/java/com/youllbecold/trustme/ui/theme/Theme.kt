package com.youllbecold.trustme.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    background = Black,
    onBackground = White,

    primary = PrettyBlue,
    secondary = WarmOrange,

    surface = DarkGrey,
    onSurface = White,

    // Floating action button
    primaryContainer = WarmOrange,
    onPrimaryContainer = White,

    // Menu highlight
    secondaryContainer = WarmOrange,
    onSecondaryContainer = White,

    errorContainer = FadedRed,
    onErrorContainer = White,
)

private val LightColorScheme = lightColorScheme(
    background = White,
    onBackground = Black,

    primary = MetalicBlue,
    secondary = PrettyBlue,

    // Floating action button
    primaryContainer = PastelFire,
    onPrimaryContainer = White,

    // Menu highlight
    secondaryContainer = PastelFire,
    onSecondaryContainer = White,
)

@Composable
fun YoullBeColdTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // False because it ruins the color scheme
    content: @Composable () -> Unit
) {
    val colorScheme = when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

    MaterialTheme(
      colorScheme = colorScheme,
      typography = Typography,
      content = content
    )
}