# Plan: ktlint + CI + Iconos de Navegación

**Branch**: `002-ktlint-ci-icons` | **Date**: 2026-05-22 | **Tasks**: [tasks.md](tasks.md)

---

## Resumen

Tres mejoras de calidad encadenadas en un solo branch:

1. **ktlint** — linter de estilo Kotlin, integrado como Gradle plugin. Corre `ktlintFormat` para limpiar el código existente y luego instala un **pre-commit hook** que bloquea commits con violaciones.
2. **GitHub Actions CI** — workflow que se activa en cada push/PR a `main` y corre `ktlintCheck` + `:shared:jvmTest` (17 tests existentes).
3. **Iconos de navegación** — sustituir los `ImageVector.Builder` manuales de `getIconForScreen()` por iconos descargados de Google Fonts (Material Symbols SVG → XML vector drawable en `composeResources/drawable/`). Este commit es la **prueba de fuego**: el hook debe pasar y el CI debe ponerse verde.

---

## Contexto técnico

**Kotlin**: 2.3.21 | **AGP**: 9.2.1 | **CMP**: 1.11.0

### ktlint-gradle plugin

- Plugin: `org.jlleitschuh.gradle.ktlint` v12.2.0 (JLLeitschuh)  
- Compatible con Kotlin 2.x y KSP.  
- Se aplica en el **root** `build.gradle.kts` para cubrir todos los módulos (`shared`, `androidApp`, `desktopApp`, `webApp`).
- Tarea relevante: `ktlintFormat` (auto-fix), `ktlintCheck` (solo verifica).

### Configuración `.editorconfig`

ktlint lee reglas desde `.editorconfig`. El proyecto Compose usa `import androidx.compose.material3.*` y otros wildcards — se deshabilitará `no-wildcard-imports` para evitar ruido. Las demás reglas estándar ktlint se mantienen activas.

```ini
[*.kt]
ktlint_standard_no-wildcard-imports = disabled
ktlint_standard_function-signature = disabled
ktlint_standard_trailing-comma-on-call-site = disabled
indent_size = 4
max_line_length = 120
```

### Pre-commit hook

Se versiona en `.githooks/pre-commit` (no en `.git/hooks/`, que no va a git):

```sh
#!/bin/sh
./gradlew ktlintCheck --daemon
```

El desarrollador ejecuta una vez: `git config core.hooksPath .githooks`.  
Se añade también una tarea Gradle `setupGitHooks` en el root `build.gradle.kts` para automatizar ese paso:

```kotlin
tasks.register("setupGitHooks") {
    doLast {
        exec { commandLine("git", "config", "core.hooksPath", ".githooks") }
    }
}
```

### GitHub Actions CI

Archivo: `.github/workflows/ci.yml`  
Trigger: `push` a cualquier branch + `pull_request` hacia `main`.  
Jobs en secuencia (lint primero, tests solo si lint pasa):

| Job | Steps |
|---|---|
| `lint` | checkout → JDK 21 (temurin) → Gradle cache → `ktlintCheck` |
| `test` | checkout → JDK 21 → Gradle cache → `:shared:jvmTest` |

Los dos jobs corren en `ubuntu-latest`. Se usa `gradle/actions/setup-gradle@v4` para el cache de Gradle.

**Jobs en secuencia:**

| Job | Tarea Gradle |
|---|---|
| `lint` | `./gradlew ktlintCheck` |
| `test` | `./gradlew :shared:jvmTest` |
| `compile-android` | `./gradlew :shared:compileKotlinAndroid` |
| `compile-ios` | `./gradlew :shared:compileKotlinIosSimulatorArm64` |

`compile-android` y `compile-ios` dependen de `test` (`needs: test`). iOS compila con el toolchain Kotlin disponible en ubuntu (no requiere Xcode — solo verifica el `commonMain` + `iosMain` source sets vía KGP).

### Iconos de navegación (Material Symbols)

**Fuente**: [fonts.google.com/icons](https://fonts.google.com/icons) — variante **Filled**, tamaño 24dp, óptica 0, peso 400.  
**Formato de descarga**: SVG → convertir a Android Vector XML.

Los 7 ítems del menú (`Screen.navItems`) y sus iconos asignados:

| Screen | Material Symbol | Archivo destino |
|---|---|---|
| Dictionary | `menu_book` | `ic_nav_dictionary.xml` |
| Meaning | `translate` | `ic_nav_meaning.xml` |
| Grammar | `school` | `ic_nav_grammar.xml` |
| Numbers | `tag` | `ic_nav_numbers.xml` |
| Quiz | `quiz` | `ic_nav_quiz.xml` |
| Credits | `groups` | `ic_nav_credits.xml` |
| Contact | `mail` | `ic_nav_contact.xml` |

**Ubicación**: `shared/src/commonMain/composeResources/drawable/`  
**Uso en código**: `painterResource(Res.drawable.ic_nav_*)` dentro del composable `Icon`.

**Conversión SVG → XML**: El SVG de Material Symbols es una sola ruta `<path d="..."/>`. El XML de vector drawable resultante tiene esta estructura:

```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
    <path android:fillColor="#000000" android:pathData="..." />
</vector>
```

**Cambio en `Screen.kt`**: introducir `NavScreen` como subtipo de `Screen` que agrega `iconRes`. `Sync` permanece como `Screen` directo (sin icono). `navItems` pasa a retornar `List<NavScreen>`.

```kotlin
sealed class Screen(val route: String, val label: String) {
    data object Sync : Screen("sync", "Cargando")

    sealed class NavScreen(route: String, label: String, val iconRes: DrawableResource)
        : Screen(route, label) {
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

**Cambio en `MuchikNavigation.kt`**: reemplazar el tipo `Screen` por `NavScreen` en las firmas de `onNavigate` y en los `forEach`. Eliminar `getIconForScreen()`. Usar `screen.iconRes` directamente con `painterResource(screen.iconRes)`.

---

## Orden de implementación

```
T001  Agregar ktlint-gradle al root build.gradle.kts + libs.versions.toml
T002  Crear .editorconfig con reglas del proyecto
T003  Correr ktlintFormat — commit "style: aplicar formato ktlint al código existente"
T004  Crear .githooks/pre-commit + tarea setupGitHooks en Gradle
T005  Ejecutar ./gradlew setupGitHooks localmente para activar el hook
T006  Crear .github/workflows/ci.yml
T007  Descargar 7 SVGs de Material Symbols y convertirlos a XML vector
T008  Añadir iconRes a Screen.kt + actualizar MuchikNavigation.kt
T009  Commit final de prueba → hook corre ktlintCheck → push → CI verde
```

T001–T006 no tienen dependencias entre sí excepto que T003 debe ir después de T002 (para que el formato respete el `.editorconfig`). T007–T009 son independientes de los anteriores y pueden hacerse en cualquier momento posterior a T001.

---

## Clarifications

### Session 2026-05-22

- Q: ¿El CI debe compilar targets nativos además de correr JVM tests? → A: Sí — jobs adicionales para `:shared:compileKotlinAndroid` y `:shared:compileKotlinIosSimulatorArm64` en secuencia tras `test`.
- Q: ¿Cómo manejar `iconRes` en `Screen.Sync` (que no es ítem de menú)? → A: Extraer `NavScreen` como subtipo de `Screen`; solo los 7 ítems de navegación extienden `NavScreen` y tienen `iconRes: DrawableResource`. `Sync` permanece como `Screen` directo sin `iconRes`.
- Q: ¿Qué reglas ktlint adicionales deshabilitar en `.editorconfig` para minimizar el diff de formato? → A: Deshabilitar `function-signature` y `trailing-comma-on-call-site` además de `no-wildcard-imports`.
- Q: ¿Un commit o varios para el diff de `ktlintFormat`? → A: Un solo commit atómico — `style: aplicar formato ktlint`.

---

## Criterios de éxito

- `./gradlew ktlintCheck` pasa sin errores en todo el proyecto.
- Intentar `git commit` con código mal formateado → el hook aborta el commit con mensaje de error.
- El push del commit final de iconos dispara el CI y ambos jobs (lint + test) quedan en verde.
- Los 7 ítems del menú muestran íconos distintos y coherentes con su sección.
