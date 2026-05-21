package dev.tohure.muchik_dictionary

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.tohure.muchik_dictionary.core.design.MuchikTheme
import dev.tohure.muchik_dictionary.core.navigation.Screen
import dev.tohure.muchik_dictionary.feature.dictionary.presentation.ui.DictionaryScreen

@Composable
fun App() {
    MuchikTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Screen.Dictionary.route,
        ) {
            composable(Screen.Dictionary.route) { DictionaryScreen() }
            composable(Screen.Meaning.route) { PlaceholderScreen("Significado de Muchik") }
            composable(Screen.Grammar.route) { PlaceholderScreen("Gramática") }
            composable(Screen.Numbers.route) { PlaceholderScreen("Sistema Numérico") }
            composable(Screen.Quiz.route) { PlaceholderScreen("Práctica") }
            composable(Screen.Credits.route) { PlaceholderScreen("Créditos") }
        }
    }
}
