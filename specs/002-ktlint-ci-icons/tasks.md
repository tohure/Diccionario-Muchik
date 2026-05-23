---
description: "Tareas — ktlint + GitHub Actions CI + Iconos de Navegación"
---

# Tasks: ktlint + CI + Iconos de Navegación

**Input**: Documentos de diseño en `specs/002-ktlint-ci-icons/`

**Prerequisites**: plan.md ✅ | spec.md ✅ | research.md ✅ | data-model.md ✅

**Tests**: No requeridos — este feature ES infraestructura de calidad. La validación es funcional (hook bloquea commits, CI queda verde).

**Organización**: Tareas agrupadas por historia de usuario para habilitar implementación y entrega independiente.

## Formato: `[ID] [P?] [Story?] Descripción con ruta de archivo`

- **[P]**: Puede ejecutarse en paralelo (archivos distintos, sin dependencias incompletas)
- **[Story]**: Historia de usuario a la que pertenece (US1–US3)

---

## Phase 1: Setup (Configuración del plugin ktlint)

**Purpose**: Instalar ktlint-gradle en el proyecto antes de cualquier otra tarea.

- [x] T001 Añadir `ktlint-gradle = "12.2.0"` en `[versions]` de `gradle/libs.versions.toml`; añadir `ktlint = { id = "org.jlleitschuh.gradle.ktlint", version.ref = "ktlint-gradle" }` en `[plugins]`
- [x] T002 Aplicar `alias(libs.plugins.ktlint)` en el bloque `plugins {}` del root `build.gradle.kts`; y también en `shared/build.gradle.kts`, `androidApp/build.gradle.kts`, `desktopApp/build.gradle.kts` y `webApp/build.gradle.kts`
- [x] T003 [P] Crear `.editorconfig` en la raíz del repositorio con: `ktlint_standard_no-wildcard-imports = disabled`, `ktlint_standard_function-signature = disabled`, `ktlint_standard_trailing-comma-on-call-site = disabled`, `indent_size = 4`, `max_line_length = 120`

**Checkpoint Setup**: `./gradlew tasks --group=verification` debe listar `ktlintCheck` y `ktlintFormat`.

---

## Phase 2: Foundational (Formato inicial y hook de pre-commit)

**Purpose**: Limpiar el código existente con `ktlintFormat` e instalar el hook que protege commits futuros. Bloquea US1 (la definición de "linter operativo" requiere código ya formateado).

**⚠️ CRÍTICO**: T004 debe completarse antes de instalar el hook (T005), para que el hook no bloquee futuros commits con el formato anterior.

- [x] T004 Ejecutar `./gradlew ktlintFormat`; verificar con `./gradlew ktlintCheck` que termina en 0 violaciones; hacer un único commit con todos los archivos modificados: `style: aplicar formato ktlint`
- [x] T005 Crear directorio `.githooks/` en la raíz del repositorio; crear archivo `.githooks/pre-commit` con contenido `#!/bin/sh\n./gradlew ktlintCheck --daemon`; ejecutar `chmod +x .githooks/pre-commit`
- [x] T006 [P] Añadir tarea `setupGitHooks` al root `build.gradle.kts` que ejecuta `git config core.hooksPath .githooks` via `exec { commandLine("git", "config", "core.hooksPath", ".githooks") }`
- [x] T007 Ejecutar `./gradlew setupGitHooks` localmente para activar el hook en el repositorio actual

**Checkpoint Foundational**: `./gradlew ktlintCheck` pasa en 0 violaciones. El hook está activo.

---

## Phase 3: User Story 1 — Linter con hook de pre-commit (Priority: P1) 🎯 MVP

**Goal**: El desarrollador no puede commitear código mal formateado sin recibir feedback inmediato del linter.

**Independent Test**: Introducir una violación deliberada en cualquier archivo `.kt` (ej. un espacio extra antes de `{`) → `git commit` debe abortar con el error de ktlint. Revertir la violación → `git commit` procede normalmente.

- [x] T008 [US1] Verificar el hook end-to-end: introducir una violación de estilo deliberada en `shared/src/commonMain/kotlin/dev/tohure/muchik_dictionary/App.kt` (ej. añadir un espacio extra); intentar `git commit` → confirmar que aborta con mensaje de error de ktlint; revertir la violación con `git checkout -- <file>`
- [x] T009 [US1] Hacer commit de los archivos del hook y la tarea Gradle: `.githooks/pre-commit`, cambios en root `build.gradle.kts`; mensaje: `feat: agregar pre-commit hook con ktlintCheck`

**Checkpoint US1**: `git commit` con código mal formateado es bloqueado. `git commit` con código limpio procede.

---

## Phase 4: User Story 2 — GitHub Actions CI (Priority: P2)

**Goal**: Cada push dispara automáticamente lint y compilación en 4 targets (lint, JVM tests, Android, iOS) detectando regresiones antes de mergear.

**Independent Test**: Push al branch → pestaña Actions de GitHub muestra 4 jobs en verde. Verificar que un commit con violación de ktlint marca el job `lint` en rojo.

- [x] T010 [P] [US2] Crear directorio `.github/workflows/` en la raíz del repositorio
- [x] T011 [US2] Crear `.github/workflows/ci.yml` con cuatro jobs en secuencia: `lint` (`./gradlew ktlintCheck`) → `test` (`./gradlew :shared:jvmTest`, needs: lint) → `compile-android` (`./gradlew :shared:compileKotlinAndroid`, needs: test) + `compile-ios` (`./gradlew :shared:compileKotlinIosSimulatorArm64`, needs: test); todos en `ubuntu-latest` con JDK 21 temurin y `gradle/actions/setup-gradle@v4`; trigger `push` a cualquier branch y `pull_request` hacia `main`

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
          with: { java-version: 21, distribution: temurin }
        - uses: gradle/actions/setup-gradle@v4
        - run: ./gradlew ktlintCheck
    test:
      runs-on: ubuntu-latest
      needs: lint
      steps:
        - uses: actions/checkout@v4
        - uses: actions/setup-java@v4
          with: { java-version: 21, distribution: temurin }
        - uses: gradle/actions/setup-gradle@v4
        - run: ./gradlew :shared:jvmTest
    compile-android:
      runs-on: ubuntu-latest
      needs: test
      steps:
        - uses: actions/checkout@v4
        - uses: actions/setup-java@v4
          with: { java-version: 21, distribution: temurin }
        - uses: gradle/actions/setup-gradle@v4
        - run: ./gradlew :shared:compileKotlinAndroid
    compile-ios:
      runs-on: ubuntu-latest
      needs: test
      steps:
        - uses: actions/checkout@v4
        - uses: actions/setup-java@v4
          with: { java-version: 21, distribution: temurin }
        - uses: gradle/actions/setup-gradle@v4
        - run: ./gradlew :shared:compileKotlinIosSimulatorArm64
  ```

- [x] T012 [US2] Hacer push del branch `002-ktlint-ci-icons` a GitHub; verificar en la pestaña Actions que los 4 jobs (lint, test, compile-android, compile-ios) quedan en verde

**Checkpoint US2**: Badge verde en GitHub Actions. Los 4 jobs pasan.

---

## Phase 5: User Story 3 — Iconos de navegación diferenciados (Priority: P3) 🔥 Prueba de fuego

**Goal**: Los 7 ítems del menú de navegación muestran íconos distintos de Material Symbols; este commit activa el hook pre-commit y el CI.

**Independent Test**: `./gradlew :desktopApp:run` → menú lateral muestra 7 íconos distintos para Dictionary, Meaning, Grammar, Numbers, Quiz, Credits, Contact.

### Descarga manual de SVGs (acción del desarrollador)

- [x] T013 [US3] Descargar los 7 SVGs de [fonts.google.com/icons](https://fonts.google.com/icons) — variante **Filled**, 24dp: `menu_book`, `translate`, `school`, `tag`, `quiz`, `groups`, `mail`; convertirlos a Android Vector XML (Android Studio: File → New → Vector Asset → Local file, o conversor online); nombrar los archivos: `ic_nav_dictionary.xml`, `ic_nav_meaning.xml`, `ic_nav_grammar.xml`, `ic_nav_numbers.xml`, `ic_nav_quiz.xml`, `ic_nav_credits.xml`, `ic_nav_contact.xml`
- [x] T014 [P] [US3] Colocar los 7 archivos XML en `shared/src/commonMain/composeResources/drawable/`

### Refactor del modelo de navegación

- [x] T015 [US3] Refactorizar `shared/src/commonMain/kotlin/dev/tohure/muchik_dictionary/core/navigation/Screen.kt`: introducir `sealed class NavScreen(route: String, label: String, val iconRes: DrawableResource) : Screen(route, label)`; mover Dictionary, Meaning, Grammar, Numbers, Quiz, Credits, Contact como subtipos de `NavScreen` con su `iconRes` correspondiente; `Screen.Sync` permanece como `Screen` directo; actualizar `navItems` para retornar `List<NavScreen>`; añadir imports de `dictionarymuchik.shared.generated.resources.*`
- [x] T016 [US3] Actualizar `shared/src/commonMain/kotlin/dev/tohure/muchik_dictionary/core/navigation/MuchikNavigation.kt`: eliminar la función `getIconForScreen()` completa (con todos sus `ImageVector.Builder`); en `MuchikNavigationRail` y `MuchikModalDrawer` reemplazar `imageVector = getIconForScreen(screen)` por `painter = painterResource(screen.iconRes)`; actualizar la firma `onNavigate: (Screen)` → `onNavigate: (NavScreen)` donde corresponda; añadir import `org.jetbrains.compose.resources.painterResource`
- [x] T017 [P] [US3] Revisar `shared/src/commonMain/kotlin/dev/tohure/muchik_dictionary/App.kt`: verificar que `onNavigate` en `AdaptiveNavigation` recibe correctamente `NavScreen`; ajustar si hay discrepancias de tipo entre `Screen` y `NavScreen` en las rutas del `NavHost`
- [x] T018 [US3] Compilar con `./gradlew :shared:compileKotlinJvm` y verificar 0 errores; ejecutar `./gradlew :desktopApp:run` y confirmar visualmente que los 7 íconos son distintos en el menú

### Commit de prueba de fuego

- [x] T019 [US3] Hacer commit con todos los cambios de iconos (`style:` o `feat:`); el **pre-commit hook** debe ejecutarse automáticamente y pasar sin errores; push al branch; verificar que GitHub Actions dispara el workflow y los 4 jobs quedan en verde

**Checkpoint US3**: 7 íconos distintos en el menú. Hook ejecutado. CI verde.

---

## Dependencies & Execution Order

### Dependencias entre fases

- **Setup (Phase 1)**: Sin dependencias — comenzar inmediatamente
- **Foundational (Phase 2)**: Requiere Phase 1 completada (plugin instalado antes de correr `ktlintFormat`)
- **US1 (Phase 3)**: Requiere Phase 2 completada (hook instalado antes de verificar)
- **US2 (Phase 4)**: Requiere Phase 2 completada; independiente de US1
- **US3 (Phase 5)**: Requiere US1 y US2 completos (el commit final es la prueba de fuego de ambos)

### Paralelismo por tarea

```
Phase 1 — T001 y T003 son paralelas (archivos distintos: toml y .editorconfig)
Phase 2 — T005 y T006 son paralelas (archivos distintos)
Phase 3 — solo T008 (verificación secuencial del hook)
Phase 4 — T010 es paralela (crear directorio no bloquea); T011 y T012 son secuenciales
Phase 5 — T013 y T014 son paralelas; T015 → T016 → T017 son secuenciales
```

---

## Implementation Strategy

### MVP First (US1 — hook operativo)

1. Completar Phase 1: Setup (T001–T003)
2. Completar Phase 2: Foundational (T004–T007)
3. Completar Phase 3: US1 — verificar hook (T008–T009)
4. **PARAR Y VALIDAR**: hook bloquea commits mal formateados

### Entrega incremental

1. Phase 1+2+US1 → hook local operativo
2. US2 → CI en GitHub
3. US3 → íconos + prueba end-to-end

---

## Notes

- `[P]` = archivos distintos, sin dependencias incompletas — pueden ejecutarse en paralelo
- `[US?]` = historia de usuario para trazabilidad con spec.md
- T013 requiere acción manual del desarrollador (descarga de SVGs)
- La variable `SUPABASE_URL`/`SUPABASE_PUBLISHABLE_KEY` no es necesaria en CI: `build.gradle.kts` usa cadena vacía como fallback cuando la env var no está definida, por lo que BuildKonfig compila sin necesitar secrets en GitHub
- El hook usa `--daemon` para acelerar la ejecución local; en CI el daemon está deshabilitado por defecto por `gradle/actions/setup-gradle`
