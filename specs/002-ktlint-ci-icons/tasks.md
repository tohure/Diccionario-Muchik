---
description: "Tareas — ktlint + CI + Iconos de Navegación"
---

# Tasks: ktlint, GitHub Actions CI e Iconos de Navegación

**Branch**: `002-ktlint-ci-icons`  
**Plan**: [plan.md](plan.md)

---

## Phase 1: ktlint — Configuración y formato inicial

**Purpose**: Instalar el linter, ajustar reglas al estilo del proyecto y limpiar el código existente.

- [ ] T001 Añadir `ktlint-gradle = "12.2.0"` en `[versions]` de `gradle/libs.versions.toml`; añadir `ktlint = { id = "org.jlleitschuh.gradle.ktlint", version.ref = "ktlint-gradle" }` en `[plugins]`
- [ ] T002 Aplicar `alias(libs.plugins.ktlint)` en el bloque `plugins {}` del root `build.gradle.kts`; también aplicarlo en `shared/build.gradle.kts`, `androidApp/build.gradle.kts`, `desktopApp/build.gradle.kts` y `webApp/build.gradle.kts`
- [ ] T003 Crear `.editorconfig` en la raíz del repositorio con las reglas del proyecto (ver plan.md §Configuración .editorconfig): `no-wildcard-imports`, `function-signature` y `trailing-comma-on-call-site` deshabilitados; `indent_size = 4`, `max_line_length = 120`
- [ ] T004 Ejecutar `./gradlew ktlintFormat` para auto-corregir todas las violaciones en el código existente; verificar con `./gradlew ktlintCheck` que termina sin errores; hacer un único commit con todos los archivos formateados: `style: aplicar formato ktlint`

**Checkpoint**: `./gradlew ktlintCheck` pasa en verde sin ninguna violación.

---

## Phase 2: Pre-commit hook

**Purpose**: Bloquear commits con código mal formateado en el entorno local de desarrollo.

- [ ] T005 Crear carpeta `.githooks/` en la raíz del repositorio y dentro el archivo `pre-commit` con contenido `#!/bin/sh\n./gradlew ktlintCheck --daemon`; darle permisos de ejecución con `chmod +x .githooks/pre-commit`
- [ ] T006 Añadir tarea `setupGitHooks` en el root `build.gradle.kts` (ver plan.md §Pre-commit hook) que ejecuta `git config core.hooksPath .githooks`
- [ ] T007 Ejecutar `./gradlew setupGitHooks` localmente para activar el hook en el repositorio
- [ ] T008 Verificar que el hook funciona: introducir una violación deliberada (espacio extra, import sin usar) en cualquier archivo `.kt`, intentar `git commit` → debe abortar con el error de ktlint; revertir la violación

**Checkpoint**: `git commit` con código mal formateado es bloqueado. `git commit` con código limpio procede normalmente.

---

## Phase 3: GitHub Actions CI

**Purpose**: Validar lint y tests automáticamente en cada push/PR.

- [ ] T009 Crear carpeta `.github/workflows/` si no existe
- [ ] T010 Crear `.github/workflows/ci.yml` con cuatro jobs en secuencia: `lint` → `test` → `compile-android` → `compile-ios`; todos en `ubuntu-latest` con JDK 21 temurin y `gradle/actions/setup-gradle@v4`; trigger en `push` a cualquier branch y `pull_request` hacia `main`

**Contenido de referencia para `ci.yml`**:
```yaml
name: CI
on:
  push:
  pull_request:
    branches: [main]
jobs:
  lint:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: 21
          distribution: temurin
      - uses: gradle/actions/setup-gradle@v4
      - run: ./gradlew ktlintCheck
  test:
    runs-on: ubuntu-latest
    needs: lint
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: 21
          distribution: temurin
      - uses: gradle/actions/setup-gradle@v4
      - run: ./gradlew :shared:jvmTest
  compile-android:
    runs-on: ubuntu-latest
    needs: test
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: 21
          distribution: temurin
      - uses: gradle/actions/setup-gradle@v4
      - run: ./gradlew :shared:compileKotlinAndroid
  compile-ios:
    runs-on: ubuntu-latest
    needs: test
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: 21
          distribution: temurin
      - uses: gradle/actions/setup-gradle@v4
      - run: ./gradlew :shared:compileKotlinIosSimulatorArm64
```

- [ ] T011 Hacer push del branch `002-ktlint-ci-icons` a GitHub; verificar en la pestaña Actions que ambos jobs (lint + test) quedan en verde

**Checkpoint**: Badge verde en GitHub Actions para este branch.

---

## Phase 4: Iconos de navegación (prueba de fuego)

**Purpose**: Sustituir los `ImageVector.Builder` manuales por recursos SVG descargados de Google Fonts. Este conjunto de cambios sirve como prueba real de que el hook pre-commit y el CI funcionan end-to-end.

### Descarga manual de SVGs

Los 7 íconos a descargar de [fonts.google.com/icons](https://fonts.google.com/icons) — variante **Filled**, 24dp:

| Screen | Nombre en Material Symbols | Archivo SVG a descargar |
|---|---|---|
| Dictionary | `menu_book` | menu_book_24dp.svg |
| Meaning | `translate` | translate_24dp.svg |
| Grammar | `school` | school_24dp.svg |
| Numbers | `tag` | tag_24dp.svg |
| Quiz | `quiz` | quiz_24dp.svg |
| Credits | `groups` | groups_24dp.svg |
| Contact | `mail` | mail_24dp.svg |

- [ ] T012 Descargar los 7 SVGs de Material Symbols (tú lo haces manualmente); convertirlos a Android Vector XML usando Android Studio (File → New → Vector Asset → Local file) o usando el conversor online de AndroidAssetStudio; nombrar los archivos `ic_nav_dictionary.xml`, `ic_nav_meaning.xml`, `ic_nav_grammar.xml`, `ic_nav_numbers.xml`, `ic_nav_quiz.xml`, `ic_nav_credits.xml`, `ic_nav_contact.xml`
- [ ] T013 Colocar los 7 archivos XML en `shared/src/commonMain/composeResources/drawable/`
- [ ] T014 Refactorizar `Screen.kt`: introducir `sealed class NavScreen(route, label, val iconRes: DrawableResource) : Screen(route, label)`; mover los 7 destinos de menú a subtipos de `NavScreen`; actualizar `navItems` para retornar `List<NavScreen>`; `Screen.Sync` permanece como `Screen` directo sin `iconRes`
- [ ] T015 Actualizar `MuchikNavigation.kt`: eliminar la función `getIconForScreen()` completa; cambiar las firmas `onNavigate: (Screen) -> Unit` → `onNavigate: (NavScreen) -> Unit` donde corresponda; reemplazar `imageVector = getIconForScreen(screen)` por `painter = painterResource(screen.iconRes)`; actualizar los imports
- [ ] T016 Compilar con `./gradlew :shared:compileKotlinJvm` y verificar que no hay errores; revisar visualmente en Desktop (`./gradlew :desktopApp:run`) que los 7 ítems del menú muestran íconos distintos

### Commit de prueba

- [ ] T017 Hacer commit con el código de iconos (código debe estar formateado — el pre-commit hook lo verificará automáticamente antes de que el commit proceda); push al branch; verificar en GitHub Actions que el CI queda verde con los jobs `lint` y `test`

**Checkpoint final**: Menú muestra 7 íconos distintos. `git commit` corría ktlintCheck automáticamente. CI verde en GitHub tras el push.

---

## Notas

- El archivo `.editorconfig` se aplica a todo el repositorio; revisarlo antes de `ktlintFormat` para no formatear archivos no-Kotlin involuntariamente (añadir `[*]` con reglas generales si es necesario).
- Si `ktlintFormat` modifica muchos archivos, hacer ese commit solo (sin mezclar con los cambios de iconos) para mantener el historial legible.
- `Screen.Sync` permanece como `Screen` directo sin `iconRes` — usa el subtipo `NavScreen` solo para los 7 ítems de menú.
- `navItems` retorna `List<NavScreen>`, lo que permite acceder a `iconRes` sin cast en los composables de navegación.
