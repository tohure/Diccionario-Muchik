package dev.tohure.muchik_dictionary.core.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.tohure.muchik_dictionary.core.design.DarkClay
import dev.tohure.muchik_dictionary.core.design.Sand
import dictionarymuchik.shared.generated.resources.Res
import dictionarymuchik.shared.generated.resources.app_title
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AdaptiveNavigation(
    currentRoute: String,
    onNavigate: (NavScreen) -> Unit,
    isWideScreen: Boolean,
    drawerState: DrawerState,
    content: @Composable () -> Unit,
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
    onNavigate: (NavScreen) -> Unit,
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
                        painter = painterResource(screen.iconRes),
                        contentDescription = screen.label
                    )
                },
                label = {
                    Text(
                        text = screen.label,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors =
                    NavigationRailItemDefaults.colors(
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
    onNavigate: (NavScreen) -> Unit,
    drawerState: DrawerState,
    content: @Composable () -> Unit,
) {
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
            ) {
                Text(
                    text = stringResource(Res.string.app_title),
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
                                painter = painterResource(screen.iconRes),
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        colors =
                            NavigationDrawerItemDefaults.colors(
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
