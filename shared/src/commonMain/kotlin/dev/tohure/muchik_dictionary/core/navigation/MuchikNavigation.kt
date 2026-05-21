package dev.tohure.muchik_dictionary.core.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.tohure.muchik_dictionary.core.design.DarkClay
import dev.tohure.muchik_dictionary.core.design.Sand
import kotlinx.coroutines.launch

@Composable
fun AdaptiveNavigation(
    currentRoute: String,
    onNavigate: (Screen) -> Unit,
    isWideScreen: Boolean,
    drawerState: DrawerState,
    content: @Composable () -> Unit
) {
    if (isWideScreen) {
        Row {
            MuchikNavigationRail(
                currentRoute = currentRoute,
                onNavigate = onNavigate
            )
            content()
        }
    } else {
        MuchikModalDrawer(
            currentRoute = currentRoute,
            onNavigate = onNavigate,
            drawerState = drawerState,
            content = content
        )
    }
}

@Composable
private fun MuchikNavigationRail(
    currentRoute: String,
    onNavigate: (Screen) -> Unit
) {
    NavigationRail(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = DarkClay,
        header = {
            Text(
                text = "M",
                style = MaterialTheme.typography.headlineMedium,
                color = DarkClay,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        },
        modifier = Modifier.fillMaxHeight()
    ) {
        Spacer(Modifier.weight(1f))
        Screen.navItems.forEach { screen ->
            NavigationRailItem(
                selected = currentRoute == screen.route,
                onClick = { onNavigate(screen) },
                icon = {
                    Icon(
                        imageVector = getIconForScreen(screen),
                        contentDescription = screen.label
                    )
                },
                label = {
                    Text(
                        text = screen.label,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = DarkClay,
                    indicatorColor = Sand,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun MuchikModalDrawer(
    currentRoute: String,
    onNavigate: (Screen) -> Unit,
    drawerState: DrawerState,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
            ) {
                Text(
                    text = "ENCICLOPEDIA MUCHIK",
                    style = MaterialTheme.typography.titleMedium,
                    color = DarkClay,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(24.dp)
                )
                Spacer(Modifier.height(8.dp))
                Screen.navItems.forEach { screen ->
                    NavigationDrawerItem(
                        label = { Text(screen.label) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            onNavigate(screen)
                            scope.launch { drawerState.close() }
                        },
                        icon = {
                            Icon(
                                imageVector = getIconForScreen(screen),
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Sand,
                            selectedTextColor = DarkClay,
                            selectedIconColor = DarkClay,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        },
        content = content
    )
}

private fun getIconForScreen(screen: Screen): ImageVector {
    return when (screen) {
        Screen.Dictionary -> ImageVector.Builder(
            name = "Dictionary",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(18f, 2f)
            lineTo(6f, 2f)
            curveTo(4.9f, 2f, 4f, 2.9f, 4f, 4f)
            verticalLineTo(20f)
            curveTo(4f, 21.1f, 4.9f, 22f, 6f, 22f)
            horizontalLineTo(18f)
            curveTo(19.1f, 22f, 20f, 21.1f, 20f, 20f)
            verticalLineTo(4f)
            curveTo(20f, 2.9f, 19.1f, 2f, 18f, 2f)
            close()
            moveTo(18f, 20f)
            horizontalLineTo(6f)
            verticalLineTo(4f)
            horizontalLineTo(7f)
            verticalLineTo(12f)
            lineTo(9.5f, 10.5f)
            lineTo(12f, 12f)
            verticalLineTo(4f)
            horizontalLineTo(18f)
            verticalLineTo(20f)
            close()
        }.build()
        Screen.Quiz -> ImageVector.Builder(
            name = "Quiz",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(19f, 5f)
            horizontalLineTo(18f)
            verticalLineTo(3f)
            horizontalLineTo(6f)
            verticalLineTo(5f)
            horizontalLineTo(5f)
            curveTo(3.9f, 5f, 3f, 5.9f, 3f, 7f)
            verticalLineTo(9f)
            curveTo(3f, 11.21f, 4.79f, 13f, 7f, 13f)
            horizontalLineTo(8.13f)
            curveTo(8.6f, 14.54f, 9.77f, 15.77f, 11f, 16.18f)
            verticalLineTo(19f)
            horizontalLineTo(9f)
            verticalLineTo(21f)
            horizontalLineTo(15f)
            verticalLineTo(19f)
            horizontalLineTo(13f)
            verticalLineTo(16.18f)
            curveTo(14.23f, 15.77f, 15.4f, 14.54f, 15.87f, 13f)
            horizontalLineTo(17f)
            curveTo(19.21f, 13f, 21f, 11.21f, 21f, 9f)
            verticalLineTo(7f)
            curveTo(21f, 5.9f, 20.1f, 5f, 19f, 5f)
            close()
            moveTo(5f, 9f)
            verticalLineTo(7f)
            horizontalLineTo(6f)
            verticalLineTo(11f)
            curveTo(4.9f, 11f, 4f, 10.1f, 4f, 9f)
            close()
            moveTo(19f, 9f)
            curveTo(19f, 10.1f, 18.1f, 11f, 17f, 11f)
            verticalLineTo(7f)
            horizontalLineTo(18f)
            verticalLineTo(9f)
            close()
        }.build()
        else -> ImageVector.Builder(
            name = "Page",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(fill = SolidColor(Color.Black)) {
            moveTo(14f, 2f)
            horizontalLineTo(6f)
            curveTo(4.9f, 2f, 4.01f, 2.9f, 4.01f, 4f)
            lineTo(4f, 20f)
            curveTo(4f, 21.1f, 4.89f, 22f, 5.99f, 22f)
            horizontalLineTo(18f)
            curveTo(19.1f, 22f, 20f, 21.1f, 20f, 20f)
            verticalLineTo(8f)
            lineTo(14f, 2f)
            close()
            moveTo(18f, 20f)
            horizontalLineTo(6f)
            verticalLineTo(4f)
            horizontalLineTo(13f)
            verticalLineTo(9f)
            horizontalLineTo(18f)
            verticalLineTo(20f)
            close()
        }.build()
    }
}
