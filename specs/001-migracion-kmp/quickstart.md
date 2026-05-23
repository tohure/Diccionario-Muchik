# Quickstart: Diccionario Muchik KMP

**Branch**: `001-migracion-kmp` | **Date**: 2026-05-22

---

## Prerrequisitos

| Herramienta | Versión mínima | Notas |
|-------------|---------------|-------|
| JDK | 17+ | `JAVA_HOME` configurado |
| Android Studio | Hedgehog+ | Con plugin KMP instalado |
| Xcode | 15+ | Solo para iOS |
| Android SDK | API 24+ | `local.properties`: `sdk.dir=...` |

---

## Configuración inicial

1. Clonar el repositorio y navegar al directorio:

   ```bash
   git clone <repo>
   cd Diccionario-Muchik
   git checkout 001-migracion-kmp
   ```

2. Crear `local.properties` en la raíz con las credenciales de Supabase:

   ```properties
   sdk.dir=/Users/<user>/Library/Android/sdk
   SUPABASE_URL=https://<project>.supabase.co
   SUPABASE_PUBLISHABLE_KEY=<tu-anon-key>
   ```

   > Sin estas claves, la app funciona en modo offline usando el corpus bundleado (414 entradas).

---

## Etapa 1 — UI (sin base de datos, datos estáticos)

### Web (WASM) — Recomendado para desarrollo inicial

```bash
./gradlew :webApp:wasmJsBrowserDevelopmentRun
```

Abre `http://localhost:8080` en el navegador.

### Desktop (JVM)

```bash
./gradlew :desktopApp:run
```

### Android

```bash
./gradlew :androidApp:installDebug
```

Requiere emulador o dispositivo conectado con ADB.

### iOS

Compilar la dependencia compartida:

```bash
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64
```

Luego abrir `iosApp/iosApp.xcodeproj` en Xcode y ejecutar en simulador.

---

## Etapa 2 — Base de datos local (Room)

Sin configuración adicional. Room 3 (androidx.room3) se inicializa automáticamente.

**Flujo de primer arranque:**
- Si `dao.count() == 0` (primera apertura): la app muestra `SyncScreen` con spinner.
  - Con red: descarga el corpus completo desde Supabase y persiste en Room.
  - Sin red: carga el corpus estático bundleado (414 entradas) desde `StaticDictionaryDataSource`.
- Aperturas posteriores: datos locales disponibles de inmediato (`SyncResult.HasLocalData`).
  El delta-sync se ejecuta en segundo plano y actualiza entradas con `updated_at > last_sync_at`.

Verificar que la BD se creó correctamente:

```bash
# Android
adb shell run-as dev.tohure.muchik_dictionary ls databases/
# Debe mostrar: muchik.db  muchik.db-shm  muchik.db-wal
```

---

## Etapa 3 — Sincronización Supabase

Con las credenciales configuradas en `local.properties`:

1. Abrir la app por primera vez → `SyncScreen` descarga el corpus completo de Supabase.
2. En aperturas posteriores → datos locales disponibles sin esperar red.
3. Presionar el botón **↻** en la pantalla de Diccionario → descarga entradas con
   `updated_at > last_sync_at` (delta-sync).

---

## Ejecutar tests

```bash
# Tests comunes (todos los targets)
./gradlew :shared:allTests

# Solo Android
./gradlew :shared:testDebugUnitTest

# Solo Desktop
./gradlew :shared:jvmTest
```

---

## Compilar verificación iOS (sin Xcode)

```bash
./gradlew :shared:compileKotlinIosSimulatorArm64
```

---

## Limpiar y reconstruir

```bash
./gradlew clean build
```

---

## Stack de dependencias clave (commonMain)

| Librería | Versión | Nota |
|----------|---------|------|
| Room 3 | `3.0.0-alpha05` | `androidx.room3.*`; FTS5; esquema v2 |
| DataStore Preferences | `1.3.0-alpha09` | Soporte JS/WASM desde alpha01 |
| Ktor Client Core | `3.5.0` | Engine por plataforma: OkHttp/Darwin/CIO/JS |
| Koin | `4.2.1` | DI compartido + `koin-compose-viewmodel` |
| Compose Multiplatform | `1.11.0` | Material3 `1.11.0-alpha07` |
| Kotlin | `2.3.21` | KSP `2.3.8` |

`mobileDesktopMain` (intermedio android/ios/jvm): solo `sqlite-bundled` + `EmojiFont.kt`.

---

## Estructura de directorios clave

```
shared/src/commonMain/kotlin/dev/tohure/muchik_dictionary/
├── App.kt                          # Composable raíz (NavHost)
├── core/
│   ├── design/                     # Tokens de diseño (colores, tipografía)
│   ├── navigation/                 # Screen destinations
│   ├── database/                   # AppDatabase, DatabaseBuilder
│   └── di/                         # AppModule, KoinInitializer
└── feature/
    ├── dictionary/                  # Búsqueda y exploración (US1)
    ├── grammar/                     # Gramática estática (US2)
    ├── quiz/                        # Práctica quiz (US3)
    ├── numbers/                     # Sistema numérico (US4)
    ├── meaning/                     # Significado de Muchik
    ├── credits/                     # Créditos
    └── sync/                        # Sincronización Supabase (US5+US6)

shared/src/commonMain/composeResources/
├── values/strings.xml              # Todas las strings de UI en español
└── files/seed_corpus.json         # Corpus bundleado (fallback offline)
```
