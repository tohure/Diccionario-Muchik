# Implementation Plan: Migración del Prototipo HTML a KMP — Diccionario Muchik

**Branch**: `001-migracion-kmp` | **Date**: 2026-05-20 | **Spec**: [spec.md](spec.md)

**Input**: Feature specification from `specs/001-migracion-kmp/spec.md`

---

## Summary

Migrar los 410+ términos del Diccionario Muchik de un prototipo HTML estático a una app
nativa Kotlin Multiplatform con Compose Multiplatform UI. La implementación se divide en
dos etapas principales: (1) **UI-first** — todas las pantallas en `commonMain` con datos
estáticos, desplegadas plataforma por plataforma (Web → Desktop → Android → iOS), y
(2) **Persistencia y Sync** — Room FTS para búsqueda offline y Supabase REST para
actualizaciones del corpus via una pantalla de loading en primera apertura y un botón
manual "Actualizar fuentes".

---

## Technical Context

**Language/Version**: Kotlin 2.3.21 / Compose Multiplatform 1.11.0

**Primary Dependencies**:
- Compose Multiplatform 1.11.0 (UI — Material3 exclusivamente; compose.material M2 eliminado)
- AGP 9.2.1 + `com.android.kotlin.multiplatform.library` plugin en `:shared`; bloque `android {}` dentro de `kotlin {}` (en KMP 2.3.21 `androidLibrary{}` está deprecado — usar `android{}` con este plugin)
- Koin 4.2.1 (DI)
- Ktor 3.0.3 (HTTP — Supabase REST sync)
- Room 2.8.4 + KSP (BD local con FTS4)
- Navigation Compose 2.9.2 (navegación)
- DataStore Preferences Core (metadata de sync)
- kotlinx.serialization 1.7.3 (JSON)
- kotlinx.coroutines 1.11.0
- BuildKonfig 0.17.1 (credenciales Supabase)

**Storage**:
- Room Multiplatform (SQLite) — corpus de palabras + FTS4
- DataStore Preferences — metadata de sincronización (2 claves)
- composeResources/files/ — seed_corpus.json bundleado (fallback)

**Testing**: kotlin-test (commonTest), kotlinx-coroutines-test, Turbine (Flow testing)

**Target Platform**: Web (WASM), Desktop (JVM), Android API 24+, iOS 16+

**Project Type**: Multiplatform mobile + desktop + web app

**Performance Goals**:
- Búsqueda FTS < 300ms para 410+ entradas (SC-002)
- Primera apertura con conexión: loading + descarga del corpus < 10s en 4G

**Constraints**:
- Offline-first (SC-003): BD local siempre disponible tras primera sincronización
- Sin secrets hardcodeados (constitución, sección Security)
- Identidad visual del prototipo HTML preservada (paleta, tipografía, card style)

**Scale/Scope**: ~410 entradas léxicas, 7 secciones, 4 plataformas, 1 desarrollador

---

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-checked after Phase 1 design.*

| Principio | Estado | Notas |
|-----------|--------|-------|
| I. Data Fidelity | ✅ | Corpus extraído del prototipo. `sourceReference` obligatorio; entradas sin fuente marcadas `TODO`. |
| II. Linguistic Accuracy | ✅ | FTS4 con tokenizador `unicode61` para diacríticos Muchik. Categoría gramatical en `category` field. |
| III. Accessibility | ✅ | UI 100% en español. Notas técnicas acompañadas de explicación en lenguaje llano. |
| IV. Full Traceability | ⚠️ | Desvío justificado: el HTML no tiene citas por entrada. Campo `sourceReference` se extrae de nota etimológica. Entradas sin fuente identificable → `TODO`. Ver Complexity Tracking. |
| V. Incremental Preservation | ✅ | UI-first por plataforma, luego BD, luego sync. Cada etapa es un entregable autónomo. |
| Digital & Tech Standards | ✅ | KMP + CMP, Koin, Ktor, Room FTS, Supabase, BuildKonfig. Mandatos respetados. |
| Contribución & Revisión | ✅ | Branch `001-migracion-kmp`. PR antes de merge a main. |

**Resultado**: GATE APROBADO con 1 desvío documentado (Principio IV).

---

## Project Structure

### Documentación (esta feature)

```
specs/001-migracion-kmp/
├── plan.md              ← este archivo
├── spec.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── supabase-rest.md
├── checklists/
│   └── requirements.md
└── tasks.md             ← generado por /speckit-tasks
```

### Código fuente

```
shared/src/commonMain/kotlin/dev/tohure/muchik_dictionary/
│
├── App.kt                                   # NavHost + KoinContext raíz
│
├── core/
│   ├── design/
│   │   ├── MuchikTheme.kt                   # MaterialTheme con tokens Muchik
│   │   ├── Color.kt                         # Paleta: Arcilla, Arena, Dorado, Océano
│   │   ├── Type.kt                          # Merriweather serif + Noto Sans
│   │   ├── CategoryColors.kt                # categoryColorMap + categoryColor()
│   │   ├── EmojiFont.kt                     # expect fun rememberEmojiFont(): FontFamily
│   │   └── LocalEmojiFontFamily.kt          # CompositionLocal para font emoji
│   ├── navigation/
│   │   ├── Screen.kt                        # sealed class con destinations tipadas
│   │   └── MuchikTopBar.kt                  # TopBar responsivo (FlowRow + BoxWithConstraints)
│   ├── database/
│   │   ├── AppDatabase.kt                   # @Database (Room)
│   │   └── DatabaseBuilder.kt               # expect/actual por plataforma
│   └── di/
│       ├── AppModule.kt                     # Aggregator de todos los módulos Koin
│       └── KoinInitializer.kt               # startKoin multiplatform
│
└── feature/
    ├── dictionary/                          # US1: Búsqueda y exploración
    │   ├── data/
    │   │   ├── local/
    │   │   │   ├── dao/WordEntryDao.kt
    │   │   │   └── entity/
    │   │   │       ├── WordEntryEntity.kt
    │   │   │       └── WordEntryFtsEntity.kt
    │   │   ├── remote/
    │   │   │   └── dto/WordEntryDto.kt      # (usado también por sync)
    │   │   ├── repository/DictionaryRepositoryImpl.kt
    │   │   └── mapper/WordEntryMapper.kt
    │   ├── domain/
    │   │   ├── model/
    │   │   │   ├── WordEntry.kt
    │   │   │   └── WordCategory.kt
    │   │   ├── repository/DictionaryRepository.kt
    │   │   └── usecase/
    │   │       ├── SearchWordsUseCase.kt
    │   │       ├── GetAllWordsUseCase.kt
    │   │       └── GetCategoryCountsUseCase.kt
    │   ├── presentation/
    │   │   ├── ui/
    │   │   │   ├── DictionaryScreen.kt
    │   │   │   ├── WordCard.kt              # Tarjeta con emoji watermark
    │   │   │   ├── WordListItem.kt          # Fila de tabla + WordListHeader
    │   │   │   ├── EmptyStateView.kt        # 🏺 sin resultados
    │   │   │   ├── DonutChart.kt            # Distribución semántica (Canvas)
    │   │   │   └── CategoryDropdown.kt      # Filtro por categoría
    │   │   ├── viewmodel/DictionaryViewModel.kt
    │   │   └── state/DictionaryUiState.kt
    │   └── di/DictionaryModule.kt
    │
    ├── grammar/                             # US2: Gramática estática
    │   └── presentation/
    │       ├── ui/
    │       │   ├── GrammarScreen.kt
    │       │   └── GrammarBlock.kt          # Composable para cada bloque
    │       └── state/GrammarUiState.kt      # minimal, mayormente estático
    │
    ├── quiz/                                # US3: Práctica quiz
    │   ├── domain/
    │   │   ├── model/QuizQuestion.kt
    │   │   └── usecase/GenerateQuizQuestionUseCase.kt
    │   ├── presentation/
    │   │   ├── ui/
    │   │   │   ├── QuizScreen.kt
    │   │   │   └── QuizOptionButton.kt
    │   │   ├── viewmodel/QuizViewModel.kt
    │   │   └── state/QuizUiState.kt
    │   └── di/QuizModule.kt
    │
    ├── numbers/                             # US4: Sistema numérico Moche
    │   ├── domain/
    │   │   └── model/
    │   │       ├── NumberEntry.kt
    │   │       └── NumeralClassifier.kt
    │   └── presentation/
    │       ├── ui/
    │       │   ├── NumbersScreen.kt
    │       │   ├── NumberCounter.kt         # Counter interactivo +/-
    │       │   └── NumeralClassifierTable.kt
    │       ├── viewmodel/NumbersViewModel.kt
    │       └── state/NumbersUiState.kt
    │
    ├── meaning/                             # Significado de Muchik (estático)
    │   └── presentation/ui/MeaningScreen.kt
    │
    ├── credits/                             # Créditos (estático)
    │   └── presentation/ui/CreditsScreen.kt
    │
    └── sync/                               # US5+US6: Offline + Sync Supabase
        ├── data/
        │   ├── remote/
        │   │   └── SyncApiService.kt        # Ktor — Supabase REST
        │   ├── local/SeedLoader.kt          # Carga seed_corpus.json
        │   └── repository/SyncRepositoryImpl.kt
        ├── domain/
        │   ├── repository/SyncRepository.kt
        │   └── usecase/
        │       ├── PerformInitialSyncUseCase.kt
        │       └── PerformDeltaSyncUseCase.kt
        ├── presentation/
        │   ├── ui/SyncLoadingScreen.kt      # Primera apertura con loading
        │   ├── viewmodel/SyncViewModel.kt
        │   └── state/SyncUiState.kt
        └── di/SyncModule.kt

shared/src/commonMain/composeResources/
├── font/
│   └── NotoEmoji-Regular.ttf               # NotoColorEmoji (COLR/CPAL) — emoji en WASM/Skia
├── values/strings.xml                       # Todos los textos de UI en español
└── files/seed_corpus.json                  # Corpus bundleado (fallback offline)

shared/src/androidMain/kotlin/.../
├── Platform.android.kt
├── database/DatabaseBuilder.android.kt     # actual DatabaseBuilder
└── core/design/EmojiFont.kt               # actual: FontFamily.Default (sistema tiene emoji)

shared/src/iosMain/kotlin/.../
├── MainViewController.kt                   # CRÍTICO: initKoin() ANTES de ComposeUIViewController
├── Platform.ios.kt
├── database/DatabaseBuilder.ios.kt
└── core/design/EmojiFont.kt               # actual: FontFamily.Default

shared/src/jvmMain/kotlin/.../
├── database/DatabaseBuilder.jvm.kt
└── core/design/EmojiFont.kt               # actual: FontFamily.Default

shared/src/wasmJsMain/kotlin/.../
├── Platform.wasmJs.kt
└── core/design/EmojiFont.kt               # actual: FontFamily(Font(Res.font.NotoEmoji_Regular))
                                            # Skia (WASM) no accede a fuentes del sistema;
                                            # NotoEmoji se carga directamente en el motor Skia
```

**Structure Decision**: Single shared module (`:shared`) con toda la lógica y UI.
Los módulos `:androidApp`, `:desktopApp`, `:webApp`, `:iosApp` son shells de 1-2 clases
que inicializan Koin y llaman a `App()`. Esto maximiza el código compartido y es
consistente con el scaffold ya existente.

---

## Complexity Tracking

> Desvíos del mandato constitucional que requieren justificación explícita

| Desvío | Por qué es necesario | Alternativa descartada y razón |
|--------|----------------------|-------------------------------|
| `sourceReference` incompleto en migración inicial (Principio IV) | El HTML no tiene citas por entrada; las notas de etimología mencionan fuentes implícitamente | Bloquear la migración hasta completar citas: retrasaría la disponibilidad del corpus sin beneficio inmediato para los usuarios. Los marcadores `TODO` permiten seguimiento editorial posterior. |
| Targets `js` y `wasmJs` coexisten en `:shared` y `:webApp` | El scaffold inicial incluye ambos targets generados por el wizard | Eliminar `js` requiere refactorizar `webApp/build.gradle.kts` y potencialmente romper dependencias: no es objetivo de esta migración. |
