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

**Cambio en `MuchikNavigation.kt`**: eliminar la función `getIconForScreen()` con los `ImageVector.Builder` manuales. Cada `Screen` obtendrá su recurso mediante `screen.iconRes: DrawableResource` añadido directamente a la sealed class `Screen`.

```kotlin
sealed class Screen(val route: String, val label: String, val iconRes: DrawableResource) {
    data object Dictionary : Screen("dictionary", "Diccionario", Res.drawable.ic_nav_dictionary)
    // ...
}
```

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

## Criterios de éxito

- `./gradlew ktlintCheck` pasa sin errores en todo el proyecto.
- Intentar `git commit` con código mal formateado → el hook aborta el commit con mensaje de error.
- El push del commit final de iconos dispara el CI y ambos jobs (lint + test) quedan en verde.
- Los 7 ítems del menú muestran íconos distintos y coherentes con su sección.
