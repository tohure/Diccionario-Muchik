package dev.tohure.muchik_dictionary

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.tohure.muchik_dictionary.core.design.LocalEmojiFontFamily
import dev.tohure.muchik_dictionary.core.design.MuchikTheme
import dev.tohure.muchik_dictionary.core.design.rememberEmojiFont
import dev.tohure.muchik_dictionary.core.navigation.AdaptiveNavigation
import dev.tohure.muchik_dictionary.core.navigation.MuchikTopBar
import dev.tohure.muchik_dictionary.core.navigation.Screen
import dev.tohure.muchik_dictionary.feature.contact.presentation.ui.ContactScreen
import dev.tohure.muchik_dictionary.feature.credits.presentation.ui.CreditsScreen
import dev.tohure.muchik_dictionary.feature.dictionary.presentation.ui.DictionaryScreen
import dev.tohure.muchik_dictionary.feature.grammar.presentation.ui.GrammarScreen
import dev.tohure.muchik_dictionary.feature.meaning.presentation.ui.MeaningScreen
import dev.tohure.muchik_dictionary.feature.numbers.presentation.ui.NumbersScreen
import dev.tohure.muchik_dictionary.feature.quiz.presentation.ui.QuizScreen
import dev.tohure.muchik_dictionary.feature.sync.presentation.ui.SyncScreen
import kotlinx.coroutines.launch

@Composable
fun App() {
    val emojiFont = rememberEmojiFont()
    MuchikTheme {
        CompositionLocalProvider(LocalEmojiFontFamily provides emojiFont) {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Dictionary.route

            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()

            BoxWithConstraints {
                val isWideScreen = maxWidth > 600.dp

                AdaptiveNavigation(
                    currentRoute = currentRoute,
                    onNavigate = { screen ->
                        navController.navigate(screen.route) {
                            popUpTo(Screen.Dictionary.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    isWideScreen = isWideScreen,
                    drawerState = drawerState
                ) {
                    Scaffold(
                        topBar = {
                            MuchikTopBar(
                                isWideScreen = isWideScreen,
                                onMenuClick = {
                                    scope.launch { drawerState.open() }
                                }
                            )
                        }
                    ) { paddingValues ->
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Sync.route,
                            modifier = Modifier.padding(paddingValues),
                        ) {
                            composable(Screen.Sync.route) {
                                SyncScreen(
                                    onSyncComplete = {
                                        navController.navigate(Screen.Dictionary.route) {
                                            popUpTo(Screen.Sync.route) { inclusive = true }
                                        }
                                    }
                                )
                            }
                            composable(Screen.Dictionary.route) { DictionaryScreen() }
                            composable(Screen.Meaning.route) { MeaningScreen() }
                            composable(Screen.Grammar.route) { GrammarScreen() }
                            composable(Screen.Numbers.route) { NumbersScreen() }
                            composable(Screen.Quiz.route) { QuizScreen() }
                            composable(Screen.Credits.route) { CreditsScreen() }
                            composable(Screen.Contact.route) { ContactScreen() }
                        }
                    }
                }
            }
        }
    }
}
