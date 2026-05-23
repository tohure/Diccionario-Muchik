---
description: "Tareas de implementación — Migración del Prototipo HTML a KMP"
---

# Tasks: Migración del Prototipo HTML a KMP — Diccionario Muchik

**Input**: Documentos de diseño en `specs/001-migracion-kmp/`

**Prerequisites**: plan.md ✅ | spec.md ✅ | research.md ✅ | data-model.md ✅ | contracts/ ✅

**Tests**: Incluidos — ViewModel/UseCase (unit) + flujos principales (integración). Exigencia explícita del usuario.

**Organización**: Tareas agrupadas por historia de usuario para habilitar implementación y entrega independiente.

## Formato: `[ID] [P?] [Story?] Descripción con ruta de archivo`

- **[P]**: Puede ejecutarse en paralelo (archivos distintos, sin dependencias incompletas)
- **[Story]**: Historia de usuario a la que pertenece (US1–US6)
- Rutas relativas desde la raíz del repositorio

---

## Phase 1: Setup (Configuración de dependencias)

**Purpose**: Añadir todas las dependencias nuevas al proyecto KMP antes de escribir código.

- [x] T001 Actualizar `gradle/libs.versions.toml` con versiones de Koin 4.1.1, Ktor 3.0.3, Room 2.8.4, Navigation 2.9.1, DataStore 1.1.1, kotlinx-serialization 1.7.3, KSP 2.3.21-1.0.32, BuildKonfig 0.17.1
- [x] T002 [P] Añadir plugins `ksp`, `room`, `kotlin-serialization`, `buildkonfig` al bloque `[plugins]` de `gradle/libs.versions.toml`
- [x] T003 Añadir dependencias de Koin, Ktor, Room, Navigation, DataStore, Serialization, BuildKonfig a `shared/build.gradle.kts` (commonMain, androidMain, iosMain, jvmMain, wasmJsMain)
- [x] T004 Aplicar plugins `ksp`, `room`, `buildkonfig` en `shared/build.gradle.kts`; configurar `room { schemaDirectory = "$projectDir/schemas" }`
- [x] T005 [P] Configurar `buildkonfig` en `shared/build.gradle.kts` con campos `SUPABASE_URL` y `SUPABASE_ANON_KEY` leídos desde `local.properties` o variables de entorno
- [x] T006 [P] Crear `shared/src/commonMain/composeResources/files/seed_corpus.json` extrayendo los 410+ términos del prototipo `prototype/muchik_old.html` (array JSON con campos: id, muchik_term, spanish_translation, category, emoji, etymology_note, source_reference)

**Checkpoint**: `./gradlew :shared:compileKotlinMetadata` debe compilar sin errores antes de continuar.

---

## Phase 2: Foundational (Arquitectura base — bloquea todas las historias)

**Purpose**: Infraestructura compartida que TODAS las historias de usuario necesitan.

**⚠️ CRÍTICO**: Ninguna historia puede comenzar hasta que esta fase esté completa.

- [x] T007 Crear `shared/src/commonMain/kotlin/dev/tohure/muchik_dictionary/core/design/Color.kt` con la paleta completa del prototipo (MuchikClay `#C06C47`, MuchikSand `#F3E5AB`, MuchikGold `#D4AF37`, MuchikOcean `#4B8F8C`, MuchikDarkClay `#8A3E26`, MuchikOffWhite `#FAF9F6`, MuchikCharcoal `#2C2C2C`)
- [x] T008 [P] Crear `shared/src/commonMain/kotlin/dev/tohure/muchik_dictionary/core/design/Type.kt` con tipografía: Merriweather serif para títulos (`h1`–`h3`), Noto Sans para cuerpo; cargar fuentes vía `composeResources/font/`
- [x] T009 [P] Crear `shared/src/commonMain/kotlin/dev/tohure/muchik_dictionary/core/design/MuchikTheme.kt` aplicando `MaterialTheme` con la paleta y tipografía Muchik
- [x] T010 Crear `shared/src/commonMain/kotlin/dev/tohure/muchik_dictionary/core/navigation/Screen.kt` como sealed class con destinations tipadas: Dictionary, Meaning, Grammar, Numbers, Quiz, Credits, Contact
- [x] T011 Reemplazar contenido de `shared/src/commonMain/kotlin/dev/tohure/muchik_dictionary/App.kt` con `NavHost` usando `rememberNavController()` y todas las rutas de `Screen.kt`; eliminar código de hello-world
- [x] T012 Crear `shared/src/commonMain/kotlin/dev/tohure/muchik_dictionary/core/di/KoinInitializer.kt` con `fun initKoin(appDeclaration: KoinAppDeclaration = {})` y `expect fun platformModule(): Module`
- [x] T013 [P] Crear `actual fun platformModule(): Module` en `androidMain`, `iosMain`, `jvmMain`, `wasmJsMain` (inicialmente vacíos o con context injection para Android)
- [x] T014 Crear `shared/src/commonMain/kotlin/dev/tohure/muchik_dictionary/core/di/AppModule.kt` como aggregator de todos los módulos Koin (inicialmente vacío, se irá completando por feature)
- [x] T015 Inicializar Koin en `:androidApp` (crear `MuchikApplication.kt` con `Application.onCreate` → `initKoin { androidContext(this) }`); registrar en `AndroidManifest.xml`
- [x] T016 [P] Inicializar Koin en `:desktopApp` (`main.kt` → `initKoin()` antes de `singleWindowApplication`)
- [x] T017 [P] Inicializar Koin en `:webApp` (main `wasmJs` → `initKoin()` antes de `renderComposable`)

**Checkpoint**: App.kt con NavHost vacío compila y muestra pantalla en blanco en Web (`./gradlew :webApp:wasmJsBrowserDevelopmentRun`).

---

## Phase 3: User Story 1 — Diccionario (Priority: P1) 🎯 MVP

**Goal**: Pantalla de búsqueda y exploración con 410+ términos, vista cards/lista, filtro por categoría, estado vacío y gráfico de distribución semántica. Datos estáticos (sin Room aún).

**Independent Test**: Abrir la app, buscar "agua" → aparece "Ja / Gu". Filtrar por "Expresiones" → solo aparecen términos de esa categoría. Buscar "xyz123" → estado vacío con 🏺.

### Tests para US1 (escribir primero, deben fallar antes de implementación)

- [x] T018 [P] [US1] Crear `shared/src/commonTest/kotlin/dev/tohure/muchik_dictionary/feature/dictionary/domain/usecase/SearchWordsUseCaseTest.kt`: verificar que buscar "agua" retorna entrada "Ja / Gu"; que buscar en término Muchik "æn" retorna resultados; que buscar "xyz123" retorna lista vacía
- [x] T019 [P] [US1] Crear `shared/src/commonTest/kotlin/dev/tohure/muchik_dictionary/feature/dictionary/presentation/DictionaryViewModelTest.kt`: verificar estado inicial (isLoading=false, entries=lista completa), transición a vacío al buscar término inexistente, filtrado correcto por categoría

### Modelos y dominio para US1

- [x] T020 [P] [US1] Crear `shared/src/commonMain/.../feature/dictionary/domain/model/WordEntry.kt` y `WordCategory.kt` (enum con 9 categorías + ALL) según `data-model.md`
- [x] T021 [P] [US1] Crear `shared/src/commonMain/.../feature/dictionary/domain/repository/DictionaryRepository.kt` interface con: `fun observeAll(): Flow<List<WordEntry>>`, `fun search(query: String): Flow<List<WordEntry>>`, `fun observeByCategory(category: WordCategory): Flow<List<WordEntry>>`, `suspend fun getCategoryCounts(): List<CategoryCount>`
- [x] T022 [US1] Crear `shared/src/commonMain/.../feature/dictionary/data/StaticDictionaryDataSource.kt` con los 410+ términos hardcodeados extraídos de `prototype/muchik_old.html` (este archivo es la fuente de verdad hasta que Room esté disponible)
- [x] T023 [US1] Crear `shared/src/commonMain/.../feature/dictionary/data/repository/DictionaryRepositoryImpl.kt` que implementa `DictionaryRepository` usando `StaticDictionaryDataSource` con filtrado en memoria + `debounce(300ms)` en el flujo de búsqueda
- [x] T024 [P] [US1] Crear `shared/src/commonMain/.../feature/dictionary/domain/usecase/SearchWordsUseCase.kt`: busca en muchikTerm, spanishTranslation y etymologyNote; retorna `Flow<List<WordEntry>>`
- [x] T025 [P] [US1] Crear `shared/src/commonMain/.../feature/dictionary/domain/usecase/GetAllWordsUseCase.kt`
- [x] T026 [P] [US1] Crear `shared/src/commonMain/.../feature/dictionary/domain/usecase/GetCategoryCountsUseCase.kt`
- [x] T027 [US1] Crear `shared/src/commonMain/.../feature/dictionary/presentation/state/DictionaryUiState.kt` (data class con: isLoading, entries, query, selectedCategory, viewMode, totalCount, categoryCounts, errorMessage)
- [x] T028 [US1] Crear `shared/src/commonMain/.../feature/dictionary/presentation/viewmodel/DictionaryViewModel.kt` con `StateFlow<DictionaryUiState>`, `SharedFlow<DictionaryEvent>`, funciones `onQueryChanged`, `onCategorySelected`, `onViewModeToggled`
- [x] T029 [P] [US1] Crear `shared/src/commonMain/.../feature/dictionary/di/DictionaryModule.kt` y registrar en `AppModule.kt`

### UI Composables para US1

- [x] T030 [P] [US1] Crear `shared/src/commonMain/.../feature/dictionary/presentation/ui/WordCard.kt`: composable con borde izquierdo arcilla (#C06C47), emoji watermark, término Muchik en bold, traducción, categoría chip, nota etimológica; hover effect `translateY(-2px)` con `animateFloatAsState`
- [x] T031 [P] [US1] Crear `shared/src/commonMain/.../feature/dictionary/presentation/ui/WordListItem.kt`: fila de tabla con columnas ícono, Muchik, Español, Categoría, Notas
- [x] T032 [P] [US1] Crear `shared/src/commonMain/.../feature/dictionary/presentation/ui/EmptyStateView.kt`: 🏺 + "No se encontraron palabras" centrado
- [x] T033 [P] [US1] Crear `shared/src/commonMain/.../feature/dictionary/presentation/ui/SemanticDistributionChart.kt`: gráfico de barras horizontales custom con `Canvas` de Compose; muestra porcentaje por categoría con color `MuchikClay`
- [x] T034 [P] [US1] Crear `shared/src/commonMain/.../feature/dictionary/presentation/ui/CategoryFilterChip.kt`: chips de categoría seleccionable con color activo `MuchikDarkClay`
- [x] T035 [US1] Crear `shared/src/commonMain/.../feature/dictionary/presentation/ui/DictionaryScreen.kt`: barra de búsqueda con ícono 🔍, dropdown de categorías, toggle cards/lista, `LazyVerticalGrid` para cards, `LazyColumn` para lista, integra todos los sub-composables anteriores y `EmptyStateView` cuando corresponde

**Checkpoint US1**: `./gradlew :shared:jvmTest` — los tests T018 y T019 pasan. Al navegar a DictionaryScreen se muestran las 410+ entradas.

---

## Phase 4: User Story 2 — Gramática Muchik (Priority: P2)

**Goal**: Pantalla con 7 bloques de contenido educativo estático completos y correctos.

**Independent Test**: Navegar a "Gramática" → ver los 7 títulos de bloque; el bloque de conjugación muestra las 6 personas gramaticales con sus marcas.

- [x] T036 [P] [US2] Crear `shared/src/commonMain/.../feature/grammar/presentation/ui/GrammarBlockCard.kt`: composable para un bloque con título en `MuchikClay`, contenido con estilos según el tipo (lista de sufijos, tabla, ejemplos bicolor)
- [x] T037 [US2] Crear `shared/src/commonMain/.../feature/grammar/presentation/ui/GrammarScreen.kt`: `LazyColumn` con los 7 bloques en orden: (1) Formación de Palabras, (2) La Regla del Peño, (3) Posposiciones Espaciales, (4) Marcas Verbales, (5) Sintaxis Básica, (6) Posesión Inalienable, (7) Préstamos Fonéticos. Contenido extraído del prototipo HTML fielmente.
- [x] T038 [P] [US2] Añadir la tabla de conjugación verbal (6 personas: Yo/-eñ, Tú/-as, Él/-ang, Nosotros/-esh, Ustedes/-aschi, Ellos/-ænang) como composable `VerbConjugationTable.kt` dentro de `grammar/presentation/ui/`
- [x] T039 [US2] Registrar `GrammarScreen` en el `NavHost` de `App.kt` y añadir enlace en la barra de navegación

**Checkpoint US2**: Navegar a Gramática → todos los 7 bloques con contenido completo, incluyendo tabla de conjugación.

---

## Phase 5: User Story 3 — Práctica Quiz (Priority: P3)

**Goal**: Quiz de vocabulario con término Muchik aleatorio, 4 opciones (1 correcta, 3 distractores), retroalimentación verde/rojo y puntaje acumulado por sesión.

**Independent Test**: Abrir Quiz → presionar "Siguiente Pregunta" → aparece un término Muchik con 4 opciones distintas. Seleccionar correcta → verde + puntaje +1. Seleccionar incorrecta → roja + correcta revelada.

### Tests para US3

- [x] T040 [P] [US3] Crear `shared/src/commonTest/kotlin/dev/tohure/muchik_dictionary/feature/quiz/domain/usecase/GenerateQuizQuestionUseCaseTest.kt`: verificar que la pregunta generada tiene exactamente 4 opciones, que la respuesta correcta está entre ellas, y que los distractores son términos distintos al correcto

### Implementación US3

- [x] T041 [P] [US3] Crear `shared/src/commonMain/.../feature/quiz/domain/model/QuizQuestion.kt`: data class con `wordEntry: WordEntry` (la correcta), `options: List<WordEntry>` (4 total, orden aleatorio)
- [x] T042 [US3] Crear `shared/src/commonMain/.../feature/quiz/domain/usecase/GenerateQuizQuestionUseCase.kt`: toma la lista completa de palabras, elige una al azar como correcta, genera 3 distractores aleatorios distintos al correcto, mezcla el orden de las 4 opciones
- [x] T043 [US3] Crear `shared/src/commonMain/.../feature/quiz/presentation/state/QuizUiState.kt` y `shared/src/commonMain/.../feature/quiz/presentation/viewmodel/QuizViewModel.kt` con `loadNextQuestion()`, `onOptionSelected(option: WordEntry)`, score acumulado en sesión
- [x] T044 [P] [US3] Crear `shared/src/commonMain/.../feature/quiz/presentation/ui/QuizOptionButton.kt`: botón con borde redondeado, estado normal/correcto (verde) / incorrecto (rojo) animado
- [x] T045 [US3] Crear `shared/src/commonMain/.../feature/quiz/presentation/ui/QuizScreen.kt`: header con gradiente arcilla, término Muchik grande, 4 botones de opciones en columna, área de feedback (texto correcto/incorrecto), footer con puntaje y botón "Siguiente Pregunta"
- [x] T046 [P] [US3] Crear `shared/src/commonMain/.../feature/quiz/di/QuizModule.kt` y registrar en `AppModule.kt`

**Checkpoint US3**: Test T040 pasa. Quiz genera preguntas variadas con distractores correctos. Puntaje acumula en sesión.

---

## Phase 6: User Story 4 — Sistema Numérico Moche (Priority: P3)

**Goal**: Pantalla con fórmula de construcción, counter interactivo +/- (1 a N) con nombre en Muchik, tabla de clasificadores numerales y lista de referencia.

**Independent Test**: Ir a Números → counter muestra "1 — Onæc". Presionar "+" → "2 — Atput". Presionar "-" en 1 → no decrementa. Tabla muestra los 8 clasificadores (Pong, Ssop, Cæss, Palæc, Chiæng, Cunô, Luc, Felæp).

- [x] T047 [P] [US4] Crear `shared/src/commonMain/.../feature/numbers/domain/model/NumberEntry.kt` y `NumeralClassifier.kt`; poblar con los datos completos del prototipo HTML (1-10 básicos + prefijos de decenas Na-/Pac-/Çoc-/Noc-/Exllmætzh-/Tzhaxlltzha-/Ñite-/Langæss-/Tap-; clasificadores Pong/Ssop/Cæss/Palæc/Chiæng/Cunô/Luc/Felæp)
- [x] T048 [US4] Crear `shared/src/commonMain/.../feature/numbers/presentation/state/NumbersUiState.kt` y `shared/src/commonMain/.../feature/numbers/presentation/viewmodel/NumbersViewModel.kt` con `onIncrement()`, `onDecrement()` (mínimo=1), `currentNumber`, `currentMuchikName`
- [x] T049 [P] [US4] Crear `shared/src/commonMain/.../feature/numbers/presentation/ui/NumberCounter.kt`: display grande con número árabe + nombre Muchik en serif, botones circulares "-" (arcilla) y "+" (océano)
- [x] T050 [P] [US4] Crear `shared/src/commonMain/.../feature/numbers/presentation/ui/NumeralClassifierTable.kt`: 3 secciones (Decenas Generales, Centenas/Millares, Sistema Dual) con sus clasificadores y descripciones
- [x] T051 [US4] Crear `shared/src/commonMain/.../feature/numbers/presentation/ui/NumbersScreen.kt`: bloque de fórmula constructiva (`[Prefijo] + [Clasificador] + allo + [Unidad] + [Objeto]`), ejemplo "12 hombres" paso a paso, counter, tabla de clasificadores, lista de referencia numérica scrolleable

**Checkpoint US4**: Counter responde correctamente a +/-; límite inferior =1; tabla muestra los 8 clasificadores.

---

## Phase 7: Pantallas estáticas y navegación completa

**Purpose**: Completar las secciones restantes (Significado, Créditos, Contacto) y la barra de navegación global.

- [x] T052 [P] Crear `shared/src/commonMain/.../feature/meaning/presentation/ui/MeaningScreen.kt`: contenido sobre la etimología de "Muchik" (raíz "Much/Mæich" + sufijo "-ik"; comparativa Yunga/Mochica/Muchik; cita de fuentes Montjoy 1865 y De la Carrera 1644) fiel al prototipo HTML
- [x] T053 [P] Crear `shared/src/commonMain/.../feature/credits/presentation/ui/CreditsScreen.kt`: dos columnas (Fuentes Históricas + Investigación Actual) y sección de comunidades (Eten, Monsefú, Mórrope, Lambayeque; familias Quesquén, Nuntón, etc.) fiel al prototipo
- [x] T054 [P] Crear `shared/src/commonMain/.../feature/contact/presentation/ui/ContactScreen.kt`: texto introductorio + enlace `mailto:cr.htorres@gmail.com` como botón prominente
- [x] T055 Crear `shared/src/commonMain/.../core/navigation/MuchikNavBar.kt`: barra de navegación inferior/lateral (adaptativa) con las 7 secciones del prototipo; marcar pestaña activa con `MuchikDarkClay` y subrayado; usar `Icons` o texto para cada sección
- [x] T056 Integrar `MuchikNavBar` en `App.kt` como elemento persistente; conectar cada pestaña a su `Screen` destination

**Checkpoint**: Todas las 7 secciones son navegables. La barra de navegación resalta la pestaña activa correctamente.

---

## Phase 8: Verificación por plataforma (UI completa)

**Purpose**: Confirmar que las pantallas compiladas en fase 3-7 funcionan correctamente en cada plataforma, con ajustes platform-specific.

### Web (WASM) — Primera plataforma objetivo

- [x] T057 Ejecutar `./gradlew :webApp:wasmJsBrowserDevelopmentRun` y verificar en navegador: (a) todas las pantallas renderizan, (b) la búsqueda funciona, (c) el counter responde, (d) el quiz genera preguntas; corregir cualquier issue de layout o composición específico de WASM
- [x] T058 [P] Crear `shared/src/commonMain/composeResources/values/strings.xml` con todos los textos de UI en español (migrar strings hardcodeados de los composables a `stringResource(Res.string.*)`)

### Desktop (JVM)

- [x] T059 Ejecutar `./gradlew :desktopApp:run` y verificar: (a) ventana redimensionable mantiene layout correcto, (b) scroll funciona con rueda de mouse, (c) interacciones de teclado en barra de búsqueda; corregir layouts adaptativos si es necesario

### Android

- [x] T060 Ejecutar `./gradlew :androidApp:installDebug` en emulador API 24+; verificar: (a) edge-to-edge con `safeContentPadding`, (b) gesto de retroceso del sistema, (c) teclado no oscurece la barra de búsqueda; corregir con `WindowInsets` si necesario
- [x] T061 [P] Añadir `android:name=".MuchikApplication"` al `AndroidManifest.xml` de `:androidApp` si no se hizo en T015; verificar que Koin inicializa sin crash

### iOS

- [x] T062 Ejecutar `./gradlew :shared:linkDebugFrameworkIosSimulatorArm64`; corregir cualquier error de compilación en `iosMain`
- [x] T063 Abrir `iosApp/iosApp.xcodeproj` en Xcode; compilar en simulador iPhone 16; verificar que `App()` se renderiza correctamente desde `MainViewController`

**Checkpoint Plataformas**: Las 7 secciones funcionan en Web, Desktop y Android. iOS compila sin errores.

---

## Phase 9: User Story 5 — Base de Datos Local Room (Priority: P2)

**Goal**: Reemplazar los datos estáticos con Room + FTS4. El corpus se carga del JSON bundleado en la primera ejecución. La búsqueda usa FTS4 con tokenizador unicode61.

**Independent Test**: Desactivar red, abrir app → corpus disponible desde BD local. Buscar "æn" → retorna términos con ese diacrítico correctamente.

### Tests para US5

- [x] T064 [P] [US5] Crear `shared/src/commonTest/kotlin/.../feature/dictionary/data/local/dao/WordEntryDaoTest.kt` con Room in-memory database: verificar `observeAll()` retorna entradas ordenadas, `searchFts("agua")` retorna "Ja / Gu", `searchFts("æn")` retorna entradas con ese patrón, `replaceAll()` elimina entradas anteriores e inserta las nuevas

### Implementación US5

- [x] T065 [US5] Crear `shared/src/commonMain/.../core/database/AppDatabase.kt` con `@Database(entities = [WordEntryEntity::class, WordEntryFtsEntity::class], version = 1)`; crear `shared/src/commonMain/.../feature/dictionary/data/local/entity/WordEntryEntity.kt` y `WordEntryFtsEntity.kt` (con `@Fts4(contentEntity, tokenizer = TOKENIZER_UNICODE61)`) según `data-model.md`
- [x] T066 [US5] Crear `shared/src/commonMain/.../feature/dictionary/data/local/dao/WordEntryDao.kt` con todos los métodos definidos en `data-model.md`: `observeAll()`, `searchFts()`, `searchLike()`, `observeByCategory()`, `count()`, `getCategoryCounts()`, `upsertAll()`, `replaceAll()`, `deleteAll()`
- [x] T067 Crear `expect class DatabaseBuilder` en `shared/src/commonMain/.../core/database/DatabaseBuilder.kt`; implementar `actual` en `androidMain` (Room.databaseBuilder con Context), `iosMain` (NSHomeDirectory path), `jvmMain` (file path en home dir), `wasmJsMain` (SQLite in-memory o archivo según soporte)
- [x] T068 [P] [US5] Crear `shared/src/commonMain/.../feature/dictionary/data/mapper/WordEntryMapper.kt` con extensiones `WordEntryDto.toEntity()`, `WordEntryEntity.toDomain()`
- [x] T069 [US5] Crear `shared/src/commonMain/.../feature/sync/data/local/SeedLoader.kt`: lee `seed_corpus.json` de composeResources con `Res.readBytes("files/seed_corpus.json")`, deserializa a `List<WordEntryDto>`, retorna `List<WordEntryEntity>` via mapper
- [x] T070 [US5] Actualizar `shared/src/commonMain/.../feature/dictionary/data/repository/DictionaryRepositoryImpl.kt` para usar `WordEntryDao` en lugar de `StaticDictionaryDataSource`; implementar lógica de búsqueda: si query tiene 3+ caracteres usa `searchFts(query + "*")` con fallback a `searchLike(query)` si FTS retorna vacío
- [x] T071 [US5] Actualizar `DictionaryModule.kt`: añadir `AppDatabase` (singleton), `WordEntryDao`, `SeedLoader` al módulo Koin; actualizar `platformModule()` en Android para inyectar `Context` al `DatabaseBuilder`
- [x] T072 [US5] Crear lógica de seed en `DictionaryRepositoryImpl` o `SyncRepository`: al inicializar, si `count() == 0`, llamar `SeedLoader` → `wordEntryDao.replaceAll()` en `Dispatchers.IO`

**Checkpoint US5**: Test T064 pasa. App funciona completamente sin conexión a internet. Búsqueda de "æn" retorna resultados con diacríticos correctos.

---

## Phase 10: User Story 6 — Sincronización Supabase (Priority: P3)

**Goal**: Primera apertura muestra loading y descarga corpus desde Supabase; aperturas posteriores usan local; botón "Actualizar fuentes" descarga delta (updated_at > lastSync).

**Independent Test**: Configurar credenciales Supabase en `local.properties`, abrir app por primera vez → pantalla de loading → diccionario lleno. Cerrar y reabrir → sin loading, datos inmediatos. Agregar término en Supabase → presionar "Actualizar fuentes" → nuevo término aparece.

### Tests para US6

- [x] T073 [P] [US6] Crear `shared/src/commonTest/kotlin/.../feature/sync/domain/usecase/PerformInitialSyncUseCaseTest.kt`: con `SyncRepository` fake, verificar que si `SYNC_COMPLETED=false` se llama `fetchAllEntries()` y se persiste en BD; verificar que si `SYNC_COMPLETED=true` NO se llama la API

### Implementación US6

- [x] T074 [P] [US6] Crear `shared/src/commonMain/.../feature/dictionary/data/remote/dto/WordEntryDto.kt` con `@Serializable` y serialización snake_case para campos Supabase
- [x] T075 [US6] Crear `shared/src/commonMain/.../feature/sync/data/remote/SyncApiService.kt` con cliente Ktor: `fetchAllEntries()` (GET /rest/v1/word_entries?select=*&order=id) y `fetchEntriesUpdatedAfter(isoDate)` (GET con filtro `updated_at=gt.<date>`); headers `apikey` y `Authorization: Bearer` con `BuildConfig.SUPABASE_ANON_KEY`
- [x] T076 [P] [US6] Crear `shared/src/commonMain/.../feature/sync/data/SyncMetadataStore.kt` con DataStore Preferences: `getSyncCompleted(): Boolean`, `setSyncCompleted(true)`, `getLastSyncDate(): String?`, `setLastSyncDate(isoDate: String)`
- [x] T077 [US6] Crear `shared/src/commonMain/.../feature/sync/domain/repository/SyncRepository.kt` interface y `SyncRepositoryImpl.kt` que orquesta `SyncApiService` + `SyncMetadataStore` + `WordEntryDao`
- [x] T078 [US6] Crear `shared/src/commonMain/.../feature/sync/domain/usecase/PerformInitialSyncUseCase.kt`: si `SYNC_COMPLETED=false` → llama API → `wordEntryDao.replaceAll()` → `setSyncCompleted(true)` + `setLastSyncDate(now)`; si falla → usar corpus bundleado via `SeedLoader`
- [x] T079 [P] [US6] Crear `shared/src/commonMain/.../feature/sync/domain/usecase/PerformDeltaSyncUseCase.kt`: llama `fetchEntriesUpdatedAfter(lastSyncDate)` → `wordEntryDao.upsertAll()` → actualiza `lastSyncDate`; si array vacío → retorna `SyncResult.AlreadyUpToDate`
- [x] T080 [US6] Crear `shared/src/commonMain/.../feature/sync/presentation/state/SyncUiState.kt` y `SyncViewModel.kt`: expone `isFirstLaunch`, `isSyncing`, `syncProgress`, `errorMessage`; lanza `PerformInitialSyncUseCase` al inicializar
- [x] T081 [US6] Crear `shared/src/commonMain/.../feature/sync/presentation/ui/SyncLoadingScreen.kt`: pantalla de bienvenida con logo/título "Diccionario Muchik", barra de progreso circular, texto "Descargando el corpus..."; navega a DictionaryScreen cuando `isSyncing=false` y sin error
- [x] T082 [US6] Actualizar lógica de inicio en `App.kt`: si `isFirstLaunch=true` mostrar `SyncLoadingScreen` como pantalla de entrada en lugar de `DictionaryScreen`
- [x] T083 [P] [US6] Añadir botón "Actualizar fuentes" en `DictionaryScreen.kt` (ícono de sync en la toolbar); al presionar llama `PerformDeltaSyncUseCase`; mostrar `LinearProgressIndicator` mientras sincroniza; snackbar de resultado (éxito / ya actualizado / error de red)
- [x] T084 [US6] Crear `shared/src/commonMain/.../feature/sync/di/SyncModule.kt`: registrar `SyncApiService`, `SyncMetadataStore`, `SyncRepositoryImpl`, `PerformInitialSyncUseCase`, `PerformDeltaSyncUseCase`, `SyncViewModel`; añadir cliente Ktor (con timeout y retry) y DataStore builder (expect/actual) al módulo

**Checkpoint US6**: Test T073 pasa. Primera apertura con Supabase configurado descarga corpus. Segunda apertura no llama la red. "Actualizar fuentes" descarga solo entradas nuevas.

---

## Phase 11: Polish & Validación final

**Purpose**: Limpieza, accesibilidad, edge cases y validación del quickstart completo.

- [x] T085 [P] Verificar que `StaticDictionaryDataSource.kt` puede eliminarse (o marcarse como deprecated/fallback) ya que Room + seed JSON lo reemplaza completamente
- [x] T086 [P] Auditar todos los composables para que usen `stringResource(Res.string.*)` en lugar de strings literales; completar `strings.xml` con cualquier string faltante
- [x] T087 [P] Añadir `contentDescription` a todos los íconos e imágenes significativas en los composables (accesibilidad TalkBack/VoiceOver)
- [x] T088 Validar edge case: primera apertura SIN conexión + SIN Room inicializado → `SeedLoader` se activa, corpus bundleado carga correctamente, sin crash
- [x] T089 [P] Validar que el campo `sourceReference` de las entradas extraídas del HTML tiene valor no vacío; las entradas con fuente no identificable deben tener `"TODO: verificar fuente primaria"` (cumplimiento Principio IV de la constitución)
- [x] T090 Ejecutar `./gradlew :shared:allTests` con todos los tests (T018, T019, T040, T064, T073); verificar que todos pasan; corregir cualquier fallo
- [x] T091 [P] Verificar quickstart completo según `specs/001-migracion-kmp/quickstart.md`: Web, Desktop y Android corren con el corpus completo y sync funcionando

---

## Dependencies & Execution Order

### Dependencias entre fases

- **Setup (Phase 1)**: Sin dependencias — comenzar inmediatamente
- **Foundational (Phase 2)**: Requiere Phase 1 completada — BLOQUEA todo lo demás
- **US1 (Phase 3)**: Requiere Phase 2 — **MVP mínimo entregable**
- **US2 (Phase 4)**: Requiere Phase 2; independiente de US1
- **US3 (Phase 5)**: Requiere Phase 2; usa listado de palabras (puede reutilizar StaticDataSource de US1)
- **US4 (Phase 6)**: Requiere Phase 2; completamente independiente
- **Platform (Phase 7)**: Requiere Phases 3–6 completas (todas las pantallas)
- **US5 Room (Phase 9)**: Requiere Phase 7; reemplaza StaticDictionaryDataSource
- **US6 Sync (Phase 10)**: Requiere Phase 9 completada
- **Polish (Phase 11)**: Requiere Phases 9–10

### Dependencias dentro de US1

Modelos (T020) → Repository interface (T021) → DataSource (T022) → RepositoryImpl (T023) →
UseCases (T024–T026) → ViewModel (T027–T028) → UI Composables (T030–T034) → Screen (T035)

Los tests T018–T019 se escriben ANTES de T022–T028 (TDD).

### Paralelismo por historia de usuario

**US1 — Paralelismo en modelos y UI:**
```
Paralelo: T020 WordEntry/Category | T021 Repository interface
Paralelo: T024 SearchUseCase | T025 GetAllUseCase | T026 CategoryCountsUseCase
Paralelo: T030 WordCard | T031 WordListItem | T032 EmptyStateView | T033 Chart | T034 FilterChip
```

**US5 Room — Paralelismo en infraestructura:**
```
Paralelo: T065 Entities/Database | T068 Mapper | T066 DAO (después de T065)
```

---

## Implementation Strategy

### MVP First (Solo US1 — Diccionario)

1. Completar Phase 1: Setup
2. Completar Phase 2: Foundational (CRÍTICO — bloquea todo)
3. Completar Phase 3: US1 Diccionario
4. **PARAR Y VALIDAR**: Checkpoint US1 — búsqueda, cards, lista, vacío, gráfico
5. Demo en Web: `./gradlew :webApp:wasmJsBrowserDevelopmentRun`

### Entrega incremental por plataforma (sugerida)

1. **Iteración 1** — Web (WASM): Phases 1–3 → demo en navegador
2. **Iteración 2** — Todas las pantallas: Phases 4–7 → todas las secciones en Web, Desktop, Android
3. **Iteración 3** — Offline: Phase 9 (Room) → corpus local, búsqueda FTS real
4. **Iteración 4** — Sync: Phase 10 (Supabase) → loading first-launch, delta sync
5. **Iteración 5** — Calidad: Phase 11 (Polish) → tests pasando, accesibilidad, iOS

### Estrategia con equipo paralelo

Si hay capacidad para trabajo paralelo tras Phase 2:
- Dev A: US1 (Diccionario) → US5 (Room) → US6 (Sync) [camino crítico]
- Dev B: US2 (Gramática) → US4 (Números) → Platform checkpoints
- Dev C: US3 (Quiz) → Polish → Testing

---

## Notes

- `[P]` = archivos distintos, sin dependencias incompletas — pueden ejecutarse en paralelo
- `[US?]` = historia de usuario para trazabilidad con spec.md
- Los tests marcados deben FALLAR antes de implementar (TDD)
- Hacer commit al terminar cada checkpoint de fase
- `StaticDictionaryDataSource` es permanente como fallback sin red y fuente de datos para tests; no eliminar
- El corpus bundleado `seed_corpus.json` es el fallback permanente para primera apertura offline
