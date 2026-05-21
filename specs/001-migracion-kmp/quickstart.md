# Quickstart: Diccionario Muchik KMP

**Branch**: `001-migracion-kmp` | **Date**: 2026-05-20

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
   SUPABASE_ANON_KEY=<tu-anon-key>
   ```

   > Sin estas claves, la app funciona en modo offline usando el corpus bundleado.

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

Sin configuración adicional. Room se inicializa automáticamente al abrir la app.
El corpus se carga del JSON bundleado si `SYNC_COMPLETED` es false.

Verificar que la BD se creó correctamente:

```bash
# Android
adb shell run-as dev.tohure.muchik_dictionary ls databases/
# Debe mostrar: muchik.db  muchik.db-shm  muchik.db-wal
```

---

## Etapa 3 — Sincronización Supabase

Con las credenciales configuradas en `local.properties`:

1. Abrir la app por primera vez → pantalla de loading descarga el corpus completo.
2. En aperturas posteriores → datos locales, sin llamadas de red.
3. Presionar "Actualizar fuentes" en cualquier pantalla → descarga entradas con
   `updated_at > LAST_SYNC_DATE`.

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
