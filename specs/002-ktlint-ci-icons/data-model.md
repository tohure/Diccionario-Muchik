# Data Model: ktlint + CI + Iconos de Navegación

**Branch**: `002-ktlint-ci-icons` | **Date**: 2026-05-22

---

## Screen / NavScreen — Jerarquía de navegación

Este es el único cambio de modelo de datos en el feature. No hay entidades de dominio nuevas.

### Antes (actual)

```kotlin
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
```

### Después (target)

```kotlin
sealed class Screen(val route: String, val label: String) {

    data object Sync : Screen("sync", "Cargando")

    sealed class NavScreen(
        route: String,
        label: String,
        val iconRes: DrawableResource
    ) : Screen(route, label) {

        data object Dictionary : NavScreen("dictionary", "Diccionario", Res.drawable.ic_nav_dictionary)
        data object Meaning    : NavScreen("meaning",    "Significado", Res.drawable.ic_nav_meaning)
        data object Grammar    : NavScreen("grammar",    "Gramática",   Res.drawable.ic_nav_grammar)
        data object Numbers    : NavScreen("numbers",    "Números",     Res.drawable.ic_nav_numbers)
        data object Quiz       : NavScreen("quiz",       "Práctica",    Res.drawable.ic_nav_quiz)
        data object Credits    : NavScreen("credits",    "Créditos",    Res.drawable.ic_nav_credits)
        data object Contact    : NavScreen("contact",    "Contacto",    Res.drawable.ic_nav_contact)
    }

    companion object {
        val navItems: List<NavScreen> get() = listOf(
            NavScreen.Dictionary, NavScreen.Meaning, NavScreen.Grammar,
            NavScreen.Numbers, NavScreen.Quiz, NavScreen.Credits, NavScreen.Contact
        )
    }
}
```

### Reglas de validación

| Regla | Descripción |
|---|---|
| `NavScreen.iconRes` requerido | El compilador garantiza que todo `NavScreen` tenga `iconRes` — no hay valor por defecto |
| `Screen.Sync` sin `iconRes` | `Sync` es `Screen` directo, no puede pasar por `navItems` |
| `navItems` retorna `List<NavScreen>` | Los composables de navegación reciben `NavScreen` y acceden a `iconRes` sin cast |

---

## Recursos de iconos

| Archivo | Screen | Material Symbol |
|---|---|---|
| `ic_nav_dictionary.xml` | NavScreen.Dictionary | `menu_book` |
| `ic_nav_meaning.xml` | NavScreen.Meaning | `translate` |
| `ic_nav_grammar.xml` | NavScreen.Grammar | `school` |
| `ic_nav_numbers.xml` | NavScreen.Numbers | `tag` |
| `ic_nav_quiz.xml` | NavScreen.Quiz | `quiz` |
| `ic_nav_credits.xml` | NavScreen.Credits | `groups` |
| `ic_nav_contact.xml` | NavScreen.Contact | `mail` |

**Ubicación**: `shared/src/commonMain/composeResources/drawable/`

**Formato**: Android Vector Drawable XML (24×24dp, `android:fillColor="#000000"`). El tint real lo aplica `NavigationRailItemDefaults.colors()` y `NavigationDrawerItemDefaults.colors()` en el composable.

---

## No hay cambios de esquema de BD

Este feature no modifica ninguna entidad de Room, DTO, ni tabla de Supabase.
