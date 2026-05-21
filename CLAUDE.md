For additional context about technologies to be used, project structure,
shell commands, and other important information, read the current plan at:
specs/001-migracion-kmp/plan.md
# Project: Muchik Dictionary KMP

You are an AI Assistant operating in the development environment of a Software Architect. Your primary goal is to help build and maintain the Muchik Dictionary using Kotlin Multiplatform (KMP) and Compose Multiplatform (CMP).

## Golden Rule
Before proposing major architectural changes or data schemas, you **MUST** read and strictly adhere to the `constitution.md` file.

## KMP Project Structure
This project uses a multi-module structure with a shared logic module and platform-specific app modules:
- `shared/src/commonMain/`: 99% of the code resides here. UI in Compose Multiplatform, Clean Architecture (Domain, Data, Presentation), dependency injection (Koin), and database (Room).
- `shared/src/androidMain/`: Android-specific code (e.g., Room initialization, Ktor engine).
- `shared/src/iosMain/`: iOS-specific code.
- `shared/src/desktopMain/`: JVM/Desktop-specific code (consumed by `:desktopApp`).
- `shared/src/wasmJsMain/`: Web-specific code (consumed by `:webApp`).
- `androidApp/`: Android application module.
- `desktopApp/`: Desktop (JVM) application module.
- `webApp/`: Web (WASM/JS) application module.
- `iosApp/`: iOS application (Xcode project).

## Main Tech Stack
- **UI:** Compose Multiplatform.
- **Architecture:** Clean Architecture + MVVM/MVI.
- **Dependency Injection:** Koin.
- **Network:** Ktor.
- **Local Database:** Room Multiplatform (FTS enabled for searches).
- **Remote Database:** Supabase KMP SDK (PostgreSQL).

## AI Agent Skills & MCP
This environment is powered by specific Agent Skills. Before writing code, generating Compose components, or configuring dependencies, you **MUST** consult the resources and guidelines located in the `.claude/skills/` folder.
- Use the `kmp-compose-multiplatform` skill to ensure Gradle dependencies and directory structures follow current standards.
- Use the `kotlin` and `android` skills to resolve complex doubts regarding concurrency, coroutines, or native integrations.

## Useful Commands (Gradle)
Use these terminal commands to build, test, and run the project depending on the target platform:
- **Android:** `./gradlew :androidApp:installDebug` (Installs on connected emulator/device).
- **Desktop (JVM):** `./gradlew :desktopApp:run` (Runs the desktop app locally).
- **Web (WASM):** `./gradlew :webApp:wasmJsBrowserDevelopmentRun` (Deploys locally in the browser).
- **iOS:** Normally executed via Xcode or Fleet, but you can verify the shared compilation with `./gradlew :shared:compileKotlinIosSimulatorArm64`.
- **Clean and Rebuild:** `./gradlew clean build`

## Code Style & Preferences
- **Imports:** Minimize the use of wildcard (`*`) imports. Be explicit.
- **Coroutines:** Never block the main thread. Use `Dispatchers.IO` for database and network operations, and `Dispatchers.Default` for heavy processing (e.g., complex dictionary transformations).
- **UI State:** Handle UI state using immutable `sealed` classes or `data classes` and expose them via `StateFlow` in ViewModels.
- **Strings (Language):** Since the base app documents the Muchik language for a Spanish-speaking audience, all business-related comments and user-facing strings MUST be written in Spanish, except for standard coding conventions which remain in English.