package com.bnyro.wallpaper.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFAFC6FF),
    onPrimary = Color(0xFF002D77),
    primaryContainer = Color(0xFF0044B1),
    onPrimaryContainer = Color(0xFFDCE8FF),
    secondary = Color(0xFF7BD0DE),
    onSecondary = Color(0xFF00363D),
    secondaryContainer = Color(0xFF004E58),
    onSecondaryContainer = Color(0xFFA5EEFC),
    tertiary = Color(0xFFFFC166),
    onTertiary = Color(0xFF432C00),
    tertiaryContainer = Color(0xFF614000),
    onTertiaryContainer = Color(0xFFFFDEB2),
    background = Color(0xFF0B1220),
    onBackground = Color(0xFFE5E9F2),
    surface = Color(0xFF121A29),
    onSurface = Color(0xFFE5E9F2),
    surfaceVariant = Color(0xFF2B3344),
    onSurfaceVariant = Color(0xFFBCC4D8),
    outline = Color(0xFF8690A6)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0B5FFF),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDCE8FF),
    onPrimaryContainer = Color(0xFF001A4D),
    secondary = Color(0xFF006875),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFA4EEFC),
    onSecondaryContainer = Color(0xFF001F24),
    tertiary = Color(0xFF7A4D00),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFDDAE),
    onTertiaryContainer = Color(0xFF281800),
    background = Color(0xFFF5F8FF),
    onBackground = Color(0xFF171C29),
    surface = Color(0xFFF8FAFF),
    onSurface = Color(0xFF171C29),
    surfaceVariant = Color(0xFFDEE3F1),
    onSurfaceVariant = Color(0xFF424958),
    outline = Color(0xFF72798A)
)

private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(10.dp),
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(30.dp)
)

@Composable
fun WallYouTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val activity = view.context as Activity
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowCompat.getInsetsController(
                    activity.window,
                    view
                ).isAppearanceLightStatusBars = !darkTheme
                WindowCompat.getInsetsController(
                    activity.window,
                    view
                ).isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}
