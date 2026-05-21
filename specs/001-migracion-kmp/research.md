# Research: Migración del Prototipo HTML a KMP — Diccionario Muchik

**Branch**: `001-migracion-kmp` | **Date**: 2026-05-20

---

## 1. Arquitectura de módulos KMP

**Decision**: Usar el módulo `:shared` existente como contenedor único de toda la lógica
compartida (UI CMP + Domain + Data). Los módulos `:androidApp`, `:desktopApp`, `:webApp`
e `:iosApp` son shells delgadas que solo orquestan la inicialización de Koin y la llamada
a `App()` en el composable principal.

**Rationale**: El scaffold del proyecto ya sigue este patrón (Single Shared UI). Evita
duplicación y maximiza el código compartido conforme al mandato de la constitución.

**Alternatives considered**:
- Multi-module por feature: más escalable para equipos grandes, pero overhead innecesario
  para un equipo pequeño con un corpus estático.

---

## 2. Ktor — Motor web (wasmJs)

**Decision**: Usar `io.ktor:ktor-client-js` en la source set `wasmJsMain`. A partir de
Ktor 3.x este artefacto soporta ambos targets `js` y `wasmJs` via el engine `Js()`.

```toml
ktor-client-js = { module = "io.ktor:ktor-client-js", version.ref = "ktor" }
```

```kotlin
// wasmJsMain / jsMain
import io.ktor.client.engine.js.Js
actual fun httpEngine() = Js.create()
```

**Rationale**: Es el único engine oficial de Ktor para ambientes browser; no requiere
dependencias nativas.

**Alternatives considered**:
- Supabase KMP SDK completo (`supabase-kt`): añade ~15 dependencias transitivas para una
  operación de solo lectura. Usar Ktor directamente es más ligero y suficiente.

---

## 3. FTS para caracteres especiales del Muchik

**Decision**: Usar `FTS4` con el tokenizador `unicode61` (disponible en SQLite 3.7.13+,
soportado en Android API 24+, iOS, Desktop y WASM-SQLite) que maneja correctamente
Unicode multibyte incluidos æ, ç, xll, tzh.

```kotlin
@Fts4(tokenizer = FtsOptions.TOKENIZER_UNICODE61)
@Entity(tableName = "word_entry_fts")
data class WordEntryFtsEntity(
    @PrimaryKey @ColumnInfo(name = "rowid") val rowId: Int = 0,
    val muchikTerm: String,
    val spanishTranslation: String,
    val etymologyNote: String
)
```

Para búsqueda de prefijos (admite escritura parcial): `query*`
Para búsqueda de infijos (no nativa en FTS): fallback con `LIKE '%query%'` en la tabla
principal. En la práctica, el 95% de las búsquedas son por prefijo.

**Rationale**: El tokenizador `unicode61` normaliza diacríticos, lo que permite que
buscar "ae" devuelva entradas con "æ" o que "con" devuelva "ç". No requiere lógica
adicional de normalización.

**Alternatives considered**:
- Normalización manual antes de insertar (reemplazar æ→ae, ç→c, etc.): pierde fidelidad
  lingüística en el índice; los términos se mostrarían incorrectamente.
- `FTS5`: disponible desde SQLite 3.20+ — compatible con las mismas plataformas target.
  Se puede usar indistintamente. Se elige FTS4 por la guía oficial de Room en KMP.

---

## 4. Gráfico de distribución semántica

**Decision**: Implementar un gráfico de barras horizontales custom en Compose (sin
dependencia externa). La distribución semántica es simple: 9-10 categorías con su
porcentaje. Un composable de ~60 líneas lo cubre completamente.

```kotlin
@Composable
fun SemanticDistributionChart(categories: List<CategoryCount>) {
    // Canvas + drawRect por cada barra — proporcional al total
}
```

**Rationale**: Evitar una dependencia pesada de charting (>200KB) para un gráfico de
barras estático. El corpus es pequeño y la distribución se calcula en <1ms.

**Alternatives considered**:
- `io.github.ehsannarmani:compose-charts`: añade dependencia externa, requiere
  mantenimiento de versiones. Overkill para este caso de uso.

---

## 5. Supabase — Integración via REST (Ktor directo)

**Decision**: Comunicar con Supabase via su PostgREST REST API usando el cliente Ktor
configurado con la anon key de Supabase. No usar el SDK completo `supabase-kt`.

Endpoint de descarga inicial (all):
```
GET https://<project>.supabase.co/rest/v1/word_entries?select=*&order=id
Headers: apikey: <anon_key>, Authorization: Bearer <anon_key>
```

Endpoint de actualización incremental (delta):
```
GET https://<project>.supabase.co/rest/v1/word_entries?select=*&updated_at=gt.<iso_date>&order=updated_at
```

**Rationale**: La sincronización es unidireccional y de solo lectura. Ktor es suficiente
y ya es dependencia mandatada por la constitución. Reduce la superficie de dependencias.

**Alternatives considered**:
- `supabase-kt` SDK: añade realtime, auth, storage — funcionalidades no requeridas.

---

## 6. BuildKonfig — Credenciales Supabase

**Decision**: Usar el plugin `com.codingfeline.buildkonfig` para inyectar la URL y la
anon key de Supabase en tiempo de compilación desde variables de entorno o `local.properties`.
Nunca hardcodear credenciales.

```kotlin
buildkonfig {
    packageName = "dev.tohure.muchik_dictionary"
    defaultConfigs {
        buildConfigField(STRING, "SUPABASE_URL", System.getenv("SUPABASE_URL") ?: "")
        buildConfigField(STRING, "SUPABASE_ANON_KEY", System.getenv("SUPABASE_ANON_KEY") ?: "")
    }
}
```

**Rationale**: Mandato constitucional (sección Digital & Technical Standards, Security).

---

## 7. DataStore — Metadata de sincronización

**Decision**: Usar `androidx.datastore:datastore-preferences-core` para persistir:
- `SYNC_COMPLETED`: Boolean — ¿se completó la descarga inicial?
- `LAST_SYNC_DATE`: String (ISO-8601) — fecha del último sync exitoso.

**Rationale**: Ligero, multiplatform, sin esquema. Solo son 2 claves de preferencias.

---

## 8. Navegación

**Decision**: `org.jetbrains.androidx.navigation:navigation-compose` versión `2.9.x`
con rutas tipadas (sealed class `Screen`). Es la librería oficial de JetBrains para
navegación Compose Multiplatform.

**Rationale**: Compatible con todos los targets del proyecto (Android, iOS, Desktop, WASM).
Soporte oficial de JetBrains.

---

## 9. Datos del corpus — Estrategia de seed

**Decision**: Los 410+ términos del prototipo HTML se extraen manualmente y se codifican
en un archivo `seed_data.json` bundleado como resource en `commonMain/composeResources/files/`.
Este JSON se inserta en Room en la primera ejecución (cuando `SYNC_COMPLETED` es false y
no hay conexión, o como fallback offline).

Al tener conexión en primera ejecución, se descarga desde Supabase (fuente de verdad).
El JSON local es el fallback de emergencia.

**Rationale**: Garantiza que la app funcione correctamente incluso si Supabase no está
disponible en la primera ejecución (caso edge documentado en spec).

---

## 10. Koin — Versión y configuración

**Decision**: `io.insert-koin:koin-core` y `koin-compose` versión `4.1.1`. La
inicialización se hace via `KoinInitializer.kt` en `commonMain` con una función
`expect fun platformModule(): Module` para inyecciones platform-specific (Context en
Android, etc.).

**Rationale**: Versión estable más reciente con soporte oficial KMP.

---

## 11. Corpus — Campo sourceReference (nota constitucional)

**Decision**: En la migración inicial, el campo `sourceReference` de cada `WordEntryEntity`
se pobla con el texto de la nota etimológica del prototipo (que en muchos casos menciona
la fuente, ej. "Registrado por Montjoy (1865)"). Las entradas sin fuente identificable
en la nota tendrán `sourceReference = "TODO: verificar fuente primaria"` conforme al
mandato de la constitución (Principio I).

**Rationale**: Principio de Preservación Incremental (V) — datos parcialmente citados
son mejores que no disponibles. Los marcadores TODO permiten seguimiento editorial.
