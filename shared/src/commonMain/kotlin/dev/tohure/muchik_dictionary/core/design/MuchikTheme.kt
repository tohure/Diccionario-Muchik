package dev.tohure.muchik_dictionary.core.design

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MuchikColorScheme = lightColorScheme(
    primary = Clay,
    onPrimary = OffWhite,
    primaryContainer = ClayContainer,
    onPrimaryContainer = DarkClay,
    secondary = Ocean,
    onSecondary = OffWhite,
    secondaryContainer = OceanContainer,
    onSecondaryContainer = Charcoal,
    tertiary = Gold,
    onTertiary = Charcoal,
    background = OffWhite,
    onBackground = Charcoal,
    surface = OffWhite,
    onSurface = Charcoal,
    surfaceVariant = SurfaceT1,
    onSurfaceVariant = DarkClay,
    outline = ClayLight,
    surfaceTint = Clay,
    error = Color(0xFFB00020),
)

@Composable
fun MuchikTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MuchikColorScheme,
        typography = getMuchikTypography(),
        shapes = MuchikShapes,
        content = content,
    )
}
