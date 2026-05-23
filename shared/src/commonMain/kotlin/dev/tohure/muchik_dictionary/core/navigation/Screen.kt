package dev.tohure.muchik_dictionary.core.navigation

sealed class Screen(val route: String, val label: String) {
    data object Sync       : Screen("sync",       "Cargando")
    data object Dictionary : Screen("dictionary", "Diccionario")
    data object Meaning    : Screen("meaning",    "Significado")
    data object Grammar    : Screen("grammar",    "Gramática")
    data object Numbers    : Screen("numbers",    "Números")
    data object Quiz       : Screen("quiz",       "Práctica")
    data object Credits    : Screen("credits",    "Créditos")
    data object Contact    : Screen("contact",    "Contacto")

    companion object {
        val navItems get() = listOf(Dictionary, Meaning, Grammar, Numbers, Quiz, Credits, Contact)
    }
}
