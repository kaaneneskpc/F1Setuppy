package com.kaaneneskpc.f1setupinstructor.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * F1 Racing Dark Color Scheme
 * Optimized for high contrast and readability with racing red accents
 */
private val F1DarkColorScheme = darkColorScheme(
    // Primary - Racing Red
    primary = RacingRed,
    onPrimary = Color.White,
    primaryContainer = RacingRedDark,
    onPrimaryContainer = Color.White,
    
    // Secondary - Gray tones
    secondary = MediumGray,
    onSecondary = Color.White,
    secondaryContainer = DarkGray,
    onSecondaryContainer = LightGray,
    
    // Tertiary
    tertiary = RacingRedLight,
    onTertiary = Color.White,
    tertiaryContainer = RacingRedDark,
    onTertiaryContainer = Color.White,
    
    // Error - Use racing red for consistency
    error = RacingRed,
    onError = Color.White,
    errorContainer = RacingRedDark,
    onErrorContainer = RacingRedLight,
    
    // Background
    background = DarkBackground,
    onBackground = Color.White,
    
    // Surface
    surface = DarkSurface,
    onSurface = Color.White,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = LightGray,
    surfaceTint = RacingRed,
    
    // Surface containers
    surfaceContainer = DarkSurface,
    surfaceContainerHigh = DarkSurfaceVariant,
    surfaceContainerHighest = DarkSurfaceContainerHighest,
    surfaceContainerLow = DarkBackground,
    surfaceContainerLowest = Color.Black,
    
    // Outline
    outline = DarkGray,
    outlineVariant = MediumGray,
    
    // Inverse
    inverseSurface = Color.White,
    inverseOnSurface = DarkBackground,
    inversePrimary = RacingRedDark,
    
    // Scrim
    scrim = Color.Black
)

/**
 * F1 Racing Light Color Scheme (for future use)
 */
private val F1LightColorScheme = lightColorScheme(
    primary = RacingRed,
    onPrimary = Color.White,
    primaryContainer = RacingRedLight,
    onPrimaryContainer = RacingRedDark,
    
    secondary = MediumGray,
    onSecondary = Color.White,
    secondaryContainer = LightGray,
    onSecondaryContainer = DarkGray,
    
    tertiary = RacingRedLight,
    onTertiary = Color.White,
    tertiaryContainer = RacingRedLight,
    onTertiaryContainer = RacingRedDark,
    
    error = RacingRed,
    onError = Color.White,
    errorContainer = RacingRedLight,
    onErrorContainer = RacingRedDark,
    
    background = Color.White,
    onBackground = DarkBackground,
    
    surface = Color(0xFFFAFAFA),
    onSurface = DarkBackground,
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = DarkGray,
    
    outline = MediumGray,
    outlineVariant = LightGray
)

/**
 * Legacy color schemes (kept for compatibility)
 */
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun F1SetupInstructorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    // Set to false to always use F1 racing colors
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> F1DarkColorScheme
        else -> F1LightColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}