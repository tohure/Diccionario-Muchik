package dev.tohure.muchik_dictionary

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.tohure.muchik_dictionary.core.design.LocalEmojiFontFamily
import dev.tohure.muchik_dictionary.core.design.MuchikTheme
import dev.tohure.muchik_dictionary.core.design.rememberEmojiFont
import dev.tohure.muchik_dictionary.core.navigation.MuchikTopBar
import dev.tohure.muchik_dictionary.core.navigation.Screen
import dev.tohure.muchik_dictionary.feature.dictionary.presentation.ui.DictionaryScreen

@Composable
fun App() {
    val emojiFont = rememberEmojiFont()
    MuchikTheme {
        CompositionLocalProvider(LocalEmojiFontFamily provides emojiFont) {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Dictionary.route

            Scaffold(
                topBar = {
                    MuchikTopBar(
                        currentRoute = currentRoute,
                        onNavigate = { screen ->
                            navController.navigate(screen.route) {
                                popUpTo(Screen.Dictionary.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                },
            ) { paddingValues ->
                NavHost(
                    navController = navController,
                    startDestination = Screen.Dictionary.route,
                    modifier = Modifier.padding(paddingValues),
                ) {
                    composable(Screen.Dictionary.route) { DictionaryScreen() }
                    composable(Screen.Meaning.route) { PlaceholderScreen("Significado de Muchik") }
                    composable(Screen.Grammar.route) { PlaceholderScreen("Gramática") }
                    composable(Screen.Numbers.route) { PlaceholderScreen("Sistema Numérico") }
                    composable(Screen.Quiz.route) { PlaceholderScreen("Práctica") }
                    composable(Screen.Credits.route) { PlaceholderScreen("Créditos") }
                    composable(Screen.Contact.route) { PlaceholderScreen("Contacto") }
                }
            }
        }
    }
}
