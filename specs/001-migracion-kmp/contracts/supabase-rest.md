# Contrato: Supabase REST API — Diccionario Muchik

**Tipo**: REST (PostgREST via Supabase)
**Acceso**: Solo lectura (anon key pública)
**Autenticación**: API key en headers

---

## Base URL

```
https://<SUPABASE_PROJECT_ID>.supabase.co
```

Configurada via `BuildConfig.SUPABASE_URL` (nunca hardcodeada).

---

## Headers comunes

```
apikey: <SUPABASE_ANON_KEY>
Authorization: Bearer <SUPABASE_ANON_KEY>
Content-Type: application/json
```

---

## Endpoints

### 1. Descarga inicial — Corpus completo

**Cuándo**: Primera apertura de la app (`SYNC_COMPLETED == false`).

```
GET /rest/v1/word_entries?select=*&order=id
```

**Response**: `200 OK` — Array JSON de `WordEntryDto`

```json
[
  {
    "id": "uuid-v4",
    "muchik_term": "Apuk",
    "spanish_translation": "Aula / Escuela",
    "category": "Educación",
    "emoji": "🏫",
    "etymology_note": "Raíz 'ap-' + sufijo locativo '-uk'.",
    "source_reference": "De la Carrera (1644) — análisis morfológico",
    "updated_at": "2026-05-20T00:00:00Z"
  }
]
```

**Error handling**:
- `401` / `403`: Clave inválida → mostrar error "Error de configuración. Contacta al desarrollador."
- `5xx` / timeout: Usar corpus JSON bundleado como fallback.

---

### 2. Actualización incremental (delta sync)

**Cuándo**: Usuario presiona "Actualizar fuentes" y `SYNC_COMPLETED == true`.

```
GET /rest/v1/word_entries?select=*&updated_at=gt.<LAST_SYNC_DATE>&order=updated_at
```

Ejemplo con fecha:
```
GET /rest/v1/word_entries?select=*&updated_at=gt.2026-05-20T00:00:00Z&order=updated_at
```

**Response**: `200 OK` — Array JSON de `WordEntryDto` (puede ser array vacío `[]`)

- Array vacío → no hay actualizaciones → mensaje "El diccionario ya está actualizado."
- Array con entradas → hacer `upsert` por `id` en Room.

**Error handling**:
- Cualquier error de red → mostrar snackbar de error, mantener datos locales actuales.

---

## Esquema de la tabla Supabase

```sql
CREATE TABLE word_entries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    muchik_term TEXT NOT NULL,
    spanish_translation TEXT NOT NULL,
    category TEXT NOT NULL,
    emoji TEXT NOT NULL DEFAULT '',
    etymology_note TEXT NOT NULL DEFAULT '',
    source_reference TEXT NOT NULL DEFAULT 'TODO: verificar fuente primaria',
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Row Level Security: lectura pública
ALTER TABLE word_entries ENABLE ROW LEVEL SECURITY;
CREATE POLICY "Lectura pública del corpus Muchik"
    ON word_entries FOR SELECT USING (true);

-- Índice para delta sync eficiente
CREATE INDEX idx_word_entries_updated_at ON word_entries (updated_at);
```

---

## Corpus JSON bundleado (fallback offline)

Ruta en el proyecto:
```
shared/src/commonMain/composeResources/files/seed_corpus.json
```

Mismo esquema que `WordEntryDto` pero en snake_case. Se usa cuando:
1. Primera apertura sin conexión a internet.
2. Error de red irrecuperable en descarga inicial.

El archivo se genera del prototipo HTML durante el proceso de extracción de datos
(ver `scripts/extract_corpus.py` — a crear en implementación).

---

## Implementación en Kotlin

```kotlin
// shared/src/commonMain/kotlin/dev/tohure/muchik_dictionary/feature/sync/data/remote/SyncApiService.kt

class SyncApiService(private val client: HttpClient) {

    suspend fun fetchAllEntries(): List<WordEntryDto> =
        client.get("/rest/v1/word_entries") {
            parameter("select", "*")
            parameter("order", "id")
        }.body()

    suspend fun fetchEntriesUpdatedAfter(isoDate: String): List<WordEntryDto> =
        client.get("/rest/v1/word_entries") {
            parameter("select", "*")
            parameter("updated_at", "gt.$isoDate")
            parameter("order", "updated_at")
        }.body()
}
```
