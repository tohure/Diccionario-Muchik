# Research: ktlint + CI + Iconos de Navegación

**Branch**: `002-ktlint-ci-icons` | **Date**: 2026-05-22

---

## ktlint-gradle v12.2.0

**Decision**: Usar `org.jlleitschuh.gradle.ktlint` v12.2.0 aplicado en el root `build.gradle.kts`.

**Rationale**:
- v12.x usa ktlint-cli 1.x internamente, compatible con Kotlin 2.x y KSP 2.3.x.
- Aplicar en root con `subprojects { apply plugin... }` vs aplicar por módulo: la forma root es más mantenible.
- Alternativas evaluadas: `kotlinter-gradle` (JMFayard) — más ligero pero menos activo; `detekt` — análisis estático más completo pero no reemplaza el formatter.

**Reglas deshabilitadas (`.editorconfig`)**:
- `no-wildcard-imports`: el código Compose usa `import androidx.compose.material3.*` — patrón válido en este dominio.
- `function-signature`: los composables tienen 5–10 parámetros; reformatearlos en líneas separadas produce diffs masivos sin valor.
- `trailing-comma-on-call-site`: Kotlin 2.x genera estas comas en algunos contextos; ktlint las exige en otros — genera conflicto. Deshabilitado para evitar ruido.

**Aplicación al proyecto**: Se aplica a `shared`, `androidApp`, `desktopApp`, `webApp`. Los archivos `.kts` (Gradle scripts) también son chequeados por defecto — se excluirán si generan demasiadas violaciones no relevantes.

---

## GitHub Actions para KMP (ubuntu-latest)

**Decision**: Cuatro jobs en secuencia: `lint` → `test` → `compile-android` + `compile-ios` (compile-android y compile-ios corren en paralelo, ambos `needs: test`).

**Rationale**:

| Job | Tarea | Plataforma | Por qué ubuntu |
|---|---|---|---|
| `lint` | `ktlintCheck` | ubuntu-latest | Solo JVM, funciona en linux |
| `test` | `:shared:jvmTest` | ubuntu-latest | Tests corren en JVM |
| `compile-android` | `:shared:compileKotlinAndroid` | ubuntu-latest | AGP compila en linux con JDK |
| `compile-ios` | `:shared:compileKotlinIosSimulatorArm64` | ubuntu-latest | Ver nota abajo |

**iOS en ubuntu-latest**: La tarea `compileKotlinIosSimulatorArm64` compila el código Kotlin a IR de Kotlin/Native. El toolchain de Kotlin/Native descarga automáticamente el compilador nativo (sysroot incluido) para el target `ios_simulator_arm64`. No requiere Xcode ni macOS. Lo que SÍ requeriría macOS es la fase de _link_ (producir el `.framework`), que usa la tarea `linkDebugFrameworkIosSimulatorArm64`. La fase de compile es suficiente para detectar errores en `commonMain` + `iosMain`.

**BuildKonfig en CI**: El job de compilación necesita las variables `SUPABASE_URL` y `SUPABASE_PUBLISHABLE_KEY` para que BuildKonfig genere el `BuildConfig.kt`. Se pasarán como GitHub Secrets: `secrets.SUPABASE_URL` y `secrets.SUPABASE_PUBLISHABLE_KEY`, inyectadas como variables de entorno en el paso de compilación.

**Alternativas evaluadas**:
- Usar `macos-latest` para iOS compile: posible pero ~3× más costoso en minutos de Actions.
- Un solo job con todos los pasos: más simple pero no hay fail-fast por categoría; si lint falla igual correría tests.

---

## Vector Drawables en Compose Multiplatform

**Decision**: SVG de Material Symbols → XML Android Vector Drawable en `shared/src/commonMain/composeResources/drawable/`.

**Rationale**:
- CMP soporta `painterResource(Res.drawable.*)` en todos los targets usando el formato Android Vector Drawable XML.
- El SVG de Material Symbols es una sola ruta `<path d="..."/>` — la conversión a XML es trivial y mecánica.
- Alternativa evaluada: `compose.material.icons.extended` — incluye todos los iconos M2 como `ImageVector` en código Kotlin; no requiere descarga de SVGs. Rechazado porque el usuario quiere Material Symbols (M3, diseño más moderno) y control explícito sobre qué iconos usar.
- Alternativa evaluada: `svg2compose` CLI — automatiza la conversión SVG→Kotlin ImageVector. Rechazado por ser una dependencia extra cuando la conversión manual es simple para 7 archivos.

**Estructura del XML**:
```xml
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="#000000" android:pathData="..." />
</vector>
```
El `fillColor="#000000"` es ignorado por Compose cuando se aplica `tint` — el color real viene de `NavigationRailItemDefaults.colors()` y `NavigationDrawerItemDefaults.colors()` ya configurados en `MuchikNavigation.kt`.

---

## Jerarquía NavScreen

**Decision**: Introducir `sealed class NavScreen(...) : Screen(...)` en lugar de `iconRes` nullable en la clase base.

**Rationale**:
- Fuerza exhaustividad en `when(screen)` — el compilador garantiza que todo `NavScreen` tiene `iconRes`.
- `Screen.Sync` no participa en la navegación del menú; tenerlo en `navItems` sería un error en tiempo de ejecución con el diseño nullable.
- Alternativa evaluada: `iconRes: DrawableResource?` con null en Sync — más simple pero pierde type-safety; cualquier uso de `screen.iconRes!!` en el futuro sería un NPE potencial.
- `navItems` cambia de `List<Screen>` a `List<NavScreen>` — los composables de navegación pueden acceder a `iconRes` sin cast ni chequeo de nulo.

**Impacto en App.kt**: Las rutas del `NavHost` siguen usando `Screen.*` (incluyendo `Screen.Sync`). El parámetro `onNavigate` en `AdaptiveNavigation` cambia de `(Screen)` a `(NavScreen)` — este cambio es compatible porque el llamador en `App.kt` recibe siempre un `NavScreen` desde `navItems`.
