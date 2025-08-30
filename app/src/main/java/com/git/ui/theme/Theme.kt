package com.git.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = Color.Black,
    primaryContainer = PurpleGrey80,
    onPrimaryContainer = Color.Black,
    secondary = PurpleGrey80,
    onSecondary = Color.Black,
    secondaryContainer = PurpleGrey80.copy(alpha = 0.2f),
    onSecondaryContainer = Color.White,
    tertiary = Pink80,
    onTertiary = Color.Black,
    background = Color(0xFF1C1B1F),
    onBackground = Color.White,
    surface = Color(0xFF2C2B2F),
    onSurface = Color.White,
    error = Color(0xFFF28B82),
    onError = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    onPrimary = Color.White,
    primaryContainer = PurpleGrey40,
    onPrimaryContainer = Color.White,
    secondary = PurpleGrey40,
    onSecondary = Color.White,
    secondaryContainer = PurpleGrey40.copy(alpha = 0.2f),
    onSecondaryContainer = Color.Black,
    tertiary = Pink40,
    onTertiary = Color.White,
    background = Color(0xFFFFFBFE),
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFFFBFE),
    onSurface = Color(0xFF1C1B1F),
    error = Color(0xFFB00020),
    onError = Color.White
)

@Composable
fun GitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true, content: @Composable () -> Unit
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
        colorScheme = colorScheme, typography = Typography, content = content
    )
}