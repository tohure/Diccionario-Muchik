package dev.tohure.muchik_dictionary.core.navigation

import dictionarymuchik.shared.generated.resources.Res
import dictionarymuchik.shared.generated.resources.ic_nav_contact
import dictionarymuchik.shared.generated.resources.ic_nav_credits
import dictionarymuchik.shared.generated.resources.ic_nav_dictionary
import dictionarymuchik.shared.generated.resources.ic_nav_grammar
import dictionarymuchik.shared.generated.resources.ic_nav_meaning
import dictionarymuchik.shared.generated.resources.ic_nav_numbers
import dictionarymuchik.shared.generated.resources.ic_nav_quiz
import org.jetbrains.compose.resources.DrawableResource

sealed class Screen(
    val route: String,
    val label: String,
) {
    data object Sync : Screen("sync", "Cargando")

    companion object {
        val navItems: List<NavScreen>
            get() = listOf(
                NavScreen.Dictionary,
                NavScreen.Meaning,
                NavScreen.Grammar,
                NavScreen.Numbers,
                NavScreen.Quiz,
                NavScreen.Credits,
                NavScreen.Contact,
            )
    }
}

sealed class NavScreen(
    route: String,
    label: String,
    val iconRes: DrawableResource,
) : Screen(route, label) {
    data object Dictionary : NavScreen("dictionary", "Diccionario", Res.drawable.ic_nav_dictionary)

    data object Meaning : NavScreen("meaning", "Significado", Res.drawable.ic_nav_meaning)

    data object Grammar : NavScreen("grammar", "Gramática", Res.drawable.ic_nav_grammar)

    data object Numbers : NavScreen("numbers", "Números", Res.drawable.ic_nav_numbers)

    data object Quiz : NavScreen("quiz", "Práctica", Res.drawable.ic_nav_quiz)

    data object Credits : NavScreen("credits", "Créditos", Res.drawable.ic_nav_credits)

    data object Contact : NavScreen("contact", "Contacto", Res.drawable.ic_nav_contact)
}
