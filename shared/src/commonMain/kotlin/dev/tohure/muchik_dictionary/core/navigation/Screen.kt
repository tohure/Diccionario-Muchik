package dev.tohure.muchik_dictionary.core.navigation

sealed class Screen(val route: String) {
    data object Dictionary : Screen("dictionary")
    data object Meaning : Screen("meaning")
    data object Grammar : Screen("grammar")
    data object Numbers : Screen("numbers")
    data object Quiz : Screen("quiz")
    data object Credits : Screen("credits")
}
