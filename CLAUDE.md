For additional context about technologies to be used, project structure,
shell commands, and other important information, read the current plan
# Project: Muchik KMP Dictionary

You are an AI Assistant operating in a Software Architect's development environment. Your primary goal is to help build and maintain the Muchik Dictionary using Kotlin Multiplatform (KMP) and Compose Multiplatform (CMP).

## Golden Rule
Before proposing major architectural changes or data schemas, you **MUST** read and strictly comply with the `constitution.md` file.

## KMP Project Structure
This project uses a Single Shared UI pattern. The main structure is within the `composeApp` module:
- `composeApp/src/commonMain/`: 99% of the code. CMP UI, Clean Architecture (Domain, Data, Presentation), DI (Koin), and Local DB (Room).
- `composeApp/src/androidMain/`: Android-specific (e.g., Room initialization, Ktor engine).
- `composeApp/src/iosMain/`: iOS-specific code.
- `composeApp/src/desktopMain/`: JVM/Desktop-specific code.
- `composeApp/src/wasmJsMain/`: Web-specific code.

## Core Tech Stack
- **UI:** Compose Multiplatform.
- **Architecture:** Clean Architecture + MVVM/MVI.
- **Dependency Injection:** Koin.
- **Networking:** Ktor.
- **Local Database:** Room Multiplatform (FTS enabled for instant search).
- **Cloud Database:** Supabase KMP SDK (PostgreSQL).

## Useful Commands (Gradle)
- **Android:** `./gradlew :composeApp:installDebug`
- **Desktop (JVM):** `./gradlew :composeApp:run`
- **Web (WASM):** `./gradlew :composeApp:wasmJsBrowserDevelopmentRun`
- **iOS:** Check shared compilation with `./gradlew :composeApp:compileKotlinIosSimulatorArm64`.
- **Clean & Rebuild:** `./gradlew clean build`

## Coding Style & Preferences
- **Imports:** Minimize wildcard imports (`*`). Be explicit.
- **Coroutines:** Never block the main thread. Use `Dispatchers.IO` for DB/Network, and `Dispatchers.Default` for heavy processing.
- **UI State:** Manage UI state using immutable `sealed` or `data classes` exposed via `StateFlow` in ViewModels.
- **Language Rule:** Code syntax (variables, functions, classes) should be in English. However, all business logic comments, UI strings, and user-facing content MUST be in Spanish (as the app documents the Muchik language for Spanish speakers).