# Diccionario Muchik

> Enciclopedia digital de la lengua Muchik (Yunga) — léxico, gramática e historia de la lengua costera del antiguo Perú.

[![CI](https://github.com/tohure/Diccionario-Muchik/actions/workflows/ci.yml/badge.svg)](https://github.com/tohure/Diccionario-Muchik/actions/workflows/ci.yml)
![Kotlin](https://img.shields.io/badge/Kotlin-2.3.21-7F52FF?logo=kotlin&logoColor=white)
![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.11.0-4285F4?logo=jetpackcompose&logoColor=white)
![Android](https://img.shields.io/badge/Android-API%2024%2B-3DDC84?logo=android&logoColor=white)
![iOS](https://img.shields.io/badge/iOS-16%2B-000000?logo=apple&logoColor=white)
![Desktop](https://img.shields.io/badge/Desktop-JVM%2021-007396?logo=openjdk&logoColor=white)
![Web](https://img.shields.io/badge/Web-WASM-654FF0?logo=webassembly&logoColor=white)
![Koin](https://img.shields.io/badge/Koin-4.2.1-F97316?logoColor=white)
![Room](https://img.shields.io/badge/Room-2.8.4-green?logo=sqlite&logoColor=white)
![Ktor](https://img.shields.io/badge/Ktor-3.5.0-0095D5?logo=ktor&logoColor=white)
![AGP](https://img.shields.io/badge/AGP-9.2.1-3DDC84?logo=android&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-yellow)

---

## Plataformas

| Plataforma | Estado |
|------------|--------|
| Android (API 24+) | ✅ |
| iOS (16+) | ✅ |
| Desktop (JVM) | ✅ |
| Web (WASM) | ✅ |

---

## Stack tecnológico

| Capa | Tecnología |
|------|------------|
| UI | Compose Multiplatform 1.11.0 |
| Arquitectura | Clean Architecture + MVVM |
| DI | Koin 4.2.1 |
| Navegación | Navigation Compose 2.9.2 |
| Base de datos local | Room Multiplatform (FTS4) |
| Red | Ktor 3.x |
| Backend | Supabase (PostgreSQL REST) |
| Fuente emoji (Web) | NotoColorEmoji (COLR/CPAL vía Compose Resources) |

---

## Estructura del proyecto

```
├── androidApp/          # Shell Android — inicializa Koin y llama App()
├── iosApp/              # Proyecto Xcode
├── desktopApp/          # Shell Desktop (JVM)
├── webApp/              # Shell Web (WASM)
├── shared/              # 99% del código — UI + lógica + datos
│   └── src/
│       ├── commonMain/  # Compose UI, Clean Architecture, Koin, Room
│       ├── androidMain/ # Room + Ktor engine Android
│       ├── iosMain/     # Room + Ktor engine Darwin
│       ├── jvmMain/     # Room + Ktor engine CIO (Desktop)
│       └── wasmJsMain/  # Font emoji (NotoColorEmoji vía resources)
└── specs/               # Especificaciones y plan de implementación
```

---

## Ejecución local

### Android
```bash
./gradlew :androidApp:installDebug
```

### Desktop (JVM)
```bash
./gradlew :desktopApp:run
```

### Web (WASM)
```bash
./gradlew :webApp:wasmJsBrowserDevelopmentRun
```

### iOS
Abre `iosApp/iosApp.xcodeproj` en Xcode, o verifica la compilación shared con:
```bash
./gradlew :shared:compileKotlinIosSimulatorArm64
```

### Build completo
```bash
./gradlew clean build
```

---

## Sobre la lengua Muchik

El **Muchik** (también llamado Yunga o Mochica) fue la lengua de los pueblos que habitaron
la costa norte del Perú durante siglos. Clasificada como lengua aislada, se extinguió como
lengua materna a principios del siglo XX. Este proyecto busca digitalizar y preservar su
corpus léxico documentado.

---

## Licencia

MIT License — ver [LICENSE](LICENSE) para detalles.
