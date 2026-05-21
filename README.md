# Diccionario Muchik KMP

Diccionario de la lengua extinta Muchik, desarrollado con Kotlin Multiplatform y Compose Multiplatform.

## 🚀 Características

- **Multiplataforma**: Funciona en Android, iOS, Desktop (JVM) y Web (WASM)
- **Arquitectura Limpia**: Separación de capas (Presentación, Dominio, Datos)
- **Base de Datos Local**: Room Multiplatform con búsqueda Full-Text para búsqueda instantánea
- **Sincronización**: Conexión con Supabase para gestión de datos en la nube
- **Diseño Adaptativo**: Experiencia de usuario nativa en todas las plataformas

## 🛠️ Tecnologías

- **Kotlin Multiplatform**: Lenguaje principal y lógica de negocio
- **Compose Multiplatform**: Interfaz de usuario unificada
- **Room Multiplatform**: Base de datos local offline-first
- **Ktor**: Cliente de red para sincronización de datos
- **Koin**: Inyección de dependencias
- **Supabase**: Backend como servicio (base de datos PostgreSQL)

## 📦 Instalación

Consulta [KMP Setup Instructions](file:///Users/tohure/Documents/Projects/Diccionario-Muchik/.specify/setup_instructions.md) para instrucciones detalladas de configuración del entorno.

## 🏃‍♂️ Ejecución

### Android
```bash
./gradlew :composeApp:installDebug
```

### Desktop (JVM)
```bash
./gradlew :composeApp:run
```

### Web (WASM)
```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

### iOS
```bash
./gradlew :composeApp:compileKotlinIosSimulatorArm64
```

## 🧹 Build
```bash
./gradlew clean build
```

## 📄 Licencia

MIT License

---

## 🧠 Guía del Asistente

Para obtener información sobre tecnologías, estructura del proyecto, comandos útiles y mejores prácticas, consulta **CLAUDE.md** y **constitution.md**.
