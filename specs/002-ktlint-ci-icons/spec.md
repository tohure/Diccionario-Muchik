# Feature Specification: ktlint + GitHub Actions CI + Iconos de Navegación

**Feature Branch**: `002-ktlint-ci-icons`

**Created**: 2026-05-22

**Status**: Clarified

---

## User Scenarios & Testing

### User Story 1 — Linter de estilo Kotlin con hook de pre-commit (Priority: P1)

Como desarrollador, quiero que el código Kotlin del proyecto siga reglas de estilo consistentes y que no pueda commitear código mal formateado sin recibir feedback inmediato.

**Why this priority**: Sin linter, las diferencias de estilo acumuladas generan ruido en los PRs y dificultan la revisión de código real.

**Independent Test**: Puede verificarse de forma independiente corriendo `./gradlew ktlintCheck` — si pasa sin errores el linter está operativo. El hook se prueba haciendo `git commit` con una violación deliberada.

**Acceptance Scenarios**:

1. **Given** código Kotlin con violación de estilo, **When** se ejecuta `./gradlew ktlintCheck`, **Then** el build falla con mensaje indicando el archivo y línea con la violación.
2. **Given** un desarrollador intenta `git commit`, **When** el código contiene violaciones de ktlint, **Then** el commit es abortado con un mensaje de error antes de crear el commit.
3. **Given** código Kotlin con formato incorrecto, **When** se ejecuta `./gradlew ktlintFormat`, **Then** el código es corregido automáticamente y `ktlintCheck` pasa sin errores.

---

### User Story 2 — CI en GitHub Actions (Priority: P2)

Como desarrollador, quiero que cada push dispare automáticamente lint y tests para detectar regresiones en cualquier target antes de mergear.

**Why this priority**: Sin CI, regresiones de compilación en targets nativos (Android, iOS) se detectan tarde.

**Independent Test**: Push al branch → verificar que el workflow aparece en la pestaña Actions de GitHub con jobs verde.

**Acceptance Scenarios**:

1. **Given** un push con código que pasa ktlintCheck y todos los tests, **When** GitHub Actions ejecuta el workflow, **Then** los cuatro jobs (lint, test, compile-android, compile-ios) quedan en verde.
2. **Given** un push con violación de ktlint, **When** el job `lint` corre, **Then** el job falla y los jobs dependientes (test, compile-android, compile-ios) no se ejecutan.
3. **Given** un push con error de compilación en `iosMain`, **When** el job `compile-ios` corre, **Then** el job falla indicando el error de compilación.

---

### User Story 3 — Iconos de navegación diferenciados (Priority: P3)

Como usuario, quiero que cada sección del menú tenga un ícono visual distinto y coherente con su contenido, en lugar del ícono genérico de página.

**Why this priority**: Mejora la navegabilidad y la identidad visual del menú. Es también la prueba de fuego del hook + CI.

**Independent Test**: Abrir la app en Desktop (`./gradlew :desktopApp:run`) y verificar que los 7 ítems del menú lateral muestran íconos distintos.

**Acceptance Scenarios**:

1. **Given** la app está abierta, **When** se ve el menú de navegación, **Then** los 7 ítems (Dictionary, Meaning, Grammar, Numbers, Quiz, Credits, Contact) muestran íconos distintos descargados de Material Symbols.
2. **Given** el código de iconos es commiteado, **When** el hook pre-commit corre, **Then** el commit procede (código formateado correctamente).
3. **Given** el código de iconos es pusheado, **When** GitHub Actions corre, **Then** los cuatro jobs quedan en verde.

---

### Edge Cases

- ¿Qué pasa si `ktlintFormat` genera un diff que rompe una prueba existente? → Imposible: ktlint solo cambia formato, no lógica.
- ¿Qué pasa si `compileKotlinIosSimulatorArm64` falla en ubuntu por falta de iOS SDK? → El Kotlin/Native toolchain incluye lo necesario para compilar (sin linkar); no requiere Xcode.
- ¿Qué pasa si `Screen.Sync` necesita un icono en el futuro? → Se puede promover a `NavScreen` en ese momento; el diseño actual no lo prohíbe.

---

## Requirements

### Functional Requirements

- **FR-001**: El proyecto MUST tener `org.jlleitschuh.gradle.ktlint` v12.2.0 aplicado en root y todos los módulos.
- **FR-002**: MUST existir un `.editorconfig` con `no-wildcard-imports`, `function-signature` y `trailing-comma-on-call-site` deshabilitados; `indent_size = 4`, `max_line_length = 120`.
- **FR-003**: MUST existir `.githooks/pre-commit` versionado en git que ejecuta `./gradlew ktlintCheck`.
- **FR-004**: MUST existir tarea Gradle `setupGitHooks` que configura `core.hooksPath`.
- **FR-005**: MUST existir `.github/workflows/ci.yml` con jobs: `lint` → `test` → `compile-android` + `compile-ios`.
- **FR-006**: Los 7 ítems de menú (`navItems`) MUST mostrar íconos de Material Symbols (Filled, 24dp) como XML vector drawables en `composeResources/drawable/`.
- **FR-007**: `Screen.kt` MUST introducir `sealed class NavScreen` como subtipo de `Screen`, con `iconRes: DrawableResource` requerido para los 7 destinos de navegación.
- **FR-008**: `Screen.Sync` MUST permanecer como `Screen` directo sin `iconRes`.

### Key Entities

- **NavScreen**: Subtipo de `Screen` para ítems de navegación; tiene `iconRes: DrawableResource`.
- **VectorDrawableIcon**: Archivo `.xml` en `composeResources/drawable/` con formato Android Vector Drawable compatible con CMP.

---

## Success Criteria

### Measurable Outcomes

- **SC-001**: `./gradlew ktlintCheck` pasa en 0 violaciones en todos los módulos.
- **SC-002**: `git commit` con violación de ktlint es abortado en < 60s (tiempo de ktlintCheck con daemon).
- **SC-003**: GitHub Actions completa los 4 jobs en < 10 minutos en ubuntu-latest.
- **SC-004**: Los 7 ítems del menú muestran íconos visualmente distintos verificados en Desktop y Android.

---

## Assumptions

- El desarrollador ejecuta `./gradlew setupGitHooks` una vez al clonar el repositorio.
- GitHub Actions tiene acceso a los secrets necesarios para `local.properties` (Supabase keys) — si el CI necesita BuildKonfig. En este branch no se ejecuta la app, solo se compila.
- Los SVGs de Material Symbols son descargados manualmente por el desarrollador y convertidos a XML antes del commit.
- `compileKotlinIosSimulatorArm64` en ubuntu-latest es suficiente para detectar errores de compilación en `iosMain` sin necesitar Xcode.
