# Feature Specification: Migración del Prototipo HTML a KMP — Diccionario Muchik

**Feature Branch**: `001-migracion-kmp`

**Created**: 2026-05-20

**Status**: Draft

**Input**: Migrar el prototipo estático HTML (`prototype/muchik_old.html`) a un proyecto
Kotlin Multiplatform por etapas — primero las pantallas UI en Web, Desktop, Android e iOS;
luego la base de datos local (Room) y la sincronización con la fuente remota (Supabase).
Respetando TDD y buenas prácticas en todo momento.

---

## User Scenarios & Testing *(mandatory)*

### User Story 1 — Explorar y Buscar el Diccionario (Priority: P1)

Un usuario (estudiante, descendiente lambayecano o investigador) abre la app y ve el
listado completo de más de 410 términos verificados del idioma Muchik con sus traducciones
al español, categoría semántica, emoji representativo y notas etimológicas. Puede buscar
en tiempo real por término Muchik, traducción española o nota etimológica; filtrar por
categoría; y alternar entre vista de tarjetas y vista de lista. Cuando no hay resultados
ve un estado vacío informativo.

**Why this priority**: Es la funcionalidad central — sin ella la app no tiene valor. Todos
los usuarios la usan como punto de entrada principal.

**Independent Test**: Se puede testear completamente cargando datos de muestra (sin BD
real) y verificando que búsquedas en español, Muchik y notas devuelvan resultados
correctos, y que el estado vacío aparezca ante búsquedas sin coincidencias.

**Acceptance Scenarios**:

1. **Given** la app está abierta, **When** el usuario escribe "agua", **Then** aparece
   la entrada "Ja / Gu" con traducción "Agua" entre los resultados.
2. **Given** hay 410+ entradas, **When** el usuario selecciona la categoría "Expresiones",
   **Then** se muestran solo los términos de esa categoría con conteo actualizado.
3. **Given** el usuario busca "xyz123", **When** no hay coincidencias, **Then** se muestra
   el estado vacío con ícono 🏺 y mensaje "No se encontraron palabras".
4. **Given** el usuario está en vista de tarjetas, **When** activa la vista lista, **Then**
   el contenido se reorganiza en tabla con columnas: Ícono, Muchik, Español, Categoría,
   Notas.

---

### User Story 2 — Aprender Gramática Muchik (Priority: P2)

Un usuario accede a la sección "Gramática" y puede leer los 7 bloques de contenido
educativo sobre la estructura del idioma: (1) Formación de palabras por sufijos,
(2) La Regla del Peño (saludos), (3) Posposiciones espaciales, (4) Marcas verbales /
conjugación, (5) Sintaxis básica (adjetivo precede sustantivo), (6) Posesión inalienable,
(7) Préstamos y evolución fonética. Cada bloque incluye ejemplos en Muchik con su
traducción al español.

**Why this priority**: Complementa el diccionario dando contexto lingüístico. Sin esta
sección la app pierde su valor enciclopédico.

**Independent Test**: Se puede testear verificando que los 7 bloques, con todo su
contenido y ejemplos del prototipo HTML, estén presentes y legibles.

**Acceptance Scenarios**:

1. **Given** el usuario navega a "Gramática", **When** la pantalla carga, **Then** ve los
   7 bloques en orden, cada uno con su título, explicación y ejemplos correctos.
2. **Given** el usuario está en el bloque de conjugación, **When** lo revisa, **Then** ve
   las 6 personas gramaticales (Yo, Tú, Él/Ella, Nosotros, Ustedes, Ellos) con sus marcas
   y ejemplos (ej. "Peñeñ chi", "Peñas chi").

---

### User Story 3 — Practicar Vocabulario con Quiz (Priority: P3)

Un usuario accede a "Práctica" y puede hacer un quiz de vocabulario: ve un término Muchik
aleatorio y debe elegir su traducción correcta entre 4 opciones. Recibe retroalimentación
inmediata (verde/rojo) y el sistema acumula su puntaje durante la sesión.

**Why this priority**: Añade valor pedagógico e interactividad, diferenciando la app de
un diccionario estático, pero no es el núcleo del producto.

**Independent Test**: Se puede testear iniciando el quiz con datos de muestra, respondiendo
preguntas y verificando que el puntaje acumule y los distractores sean aleatorios y
plausibles.

**Acceptance Scenarios**:

1. **Given** el usuario está en "Práctica", **When** presiona "Siguiente Pregunta", **Then**
   ve un término Muchik con 4 opciones (1 correcta, 3 aleatorias del corpus).
2. **Given** el usuario selecciona la respuesta correcta, **When** confirma, **Then** la
   opción se resalta en verde y el puntaje incrementa en 1.
3. **Given** el usuario selecciona una respuesta incorrecta, **When** confirma, **Then**
   la opción incorrecta se resalta en rojo y se revela la traducción correcta.

---

### User Story 4 — Explorar el Sistema Numérico Moche (Priority: P3)

Un usuario accede a "Números" y puede: (a) ver la fórmula de construcción de números
compuestos con clasificadores numerales (Pong, Ssop, Cæss, Palæc, etc.), (b) usar un
contador interactivo (+/-) que muestra el número cardinal en Muchik, y (c) consultar la
referencia completa de unidades básicas.

**Why this priority**: Contenido único y diferenciador sobre el sistema matemático Moche,
pero accesorio frente al diccionario y la gramática.

**Independent Test**: Se puede testear mostrando la fórmula, verificando que el counter
responde a +/- y que la lista de números se renderiza completa.

**Acceptance Scenarios**:

1. **Given** el usuario está en "Números" y el counter muestra "1 — Onæc", **When**
   presiona "+", **Then** el counter muestra "2 — Atput".
2. **Given** el counter está en 1, **When** el usuario presiona "-", **Then** el counter
   no decrementa (límite inferior = 1).
3. **Given** la pantalla carga, **When** el usuario revisa la tabla de clasificadores,
   **Then** ve los 8 clasificadores (Pong, Ssop, Cæss, Palæc, Chiæng, Cunô, Luc, Felæp)
   con sus descripciones de uso.

---

### User Story 5 — Uso Sin Conexión a Internet (Priority: P2)

Un usuario en una zona rural de Lambayeque o investigador en campo puede usar el
diccionario, buscar términos y acceder a todo el contenido educativo sin conexión a
internet. El corpus completo reside localmente en el dispositivo.

**Why this priority**: El público objetivo incluye zonas con conectividad limitada. La
disponibilidad offline es un requisito no negociable de la constitución del proyecto.

**Independent Test**: Se puede testear desactivando completamente la red y verificando
que todas las funcionalidades (búsqueda, gramática, quiz, números) operen sin error.

**Acceptance Scenarios**:

1. **Given** el dispositivo no tiene conexión, **When** el usuario busca cualquier término,
   **Then** los resultados aparecen desde la base de datos local sin ningún error.
2. **Given** es la primera apertura de la app con conexión disponible, **When** la
   descarga inicial del corpus termina, **Then** el diccionario completo queda disponible
   para uso offline.

---

### User Story 6 — Contenido Siempre Actualizado vía Sincronización (Priority: P3)

Un usuario con la app instalada puede recibir nuevas entradas, correcciones o mejoras
etimológicas desde la fuente remota sin necesidad de actualizar la app. La sincronización
es transparente y no interrumpe el uso.

**Why this priority**: Permite evolucionar el corpus lingüístico independientemente del
ciclo de releases de la app, haciendo el proyecto sostenible a largo plazo.

**Independent Test**: Se puede testear agregando una entrada en la fuente remota y
verificando que la app la refleje tras la sincronización.

**Acceptance Scenarios**:

1. **Given** se añade un término nuevo en la fuente remota, **When** el usuario presiona
   "Actualizar fuentes" y la descarga termina, **Then** el término nuevo aparece en el
   diccionario local.
2. **Given** la sincronización está en curso, **When** el usuario usa el buscador al mismo
   tiempo, **Then** la búsqueda no se interrumpe ni se ralentiza perceptiblemente.

---

### Edge Cases

- ¿Qué ocurre si la fuente remota no está disponible durante la primera instalación y el
  corpus local está vacío?
- ¿Cómo se manejan los caracteres especiales Muchik (æ, ç, xll, tzh) en la búsqueda FTS
  para que "mæcya" y "macya" devuelvan el mismo resultado?
- ¿Qué sucede si el usuario cierra la app durante la sincronización inicial?
- ¿Se muestra un indicador de progreso al descargar el corpus completo por primera vez?

---

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema MUST mostrar los 410+ términos verificados con: término Muchik,
  traducción española, categoría semántica, emoji y notas etimológicas.
- **FR-002**: El sistema MUST ofrecer búsqueda en tiempo real (con debouncing) sobre tres
  campos simultáneos: término Muchik, traducción española y notas etimológicas.
- **FR-003**: El sistema MUST permitir filtrar el diccionario por categoría semántica
  (Educación, Expresiones, Tiempo, Conceptos, Naturaleza, Toponimia, Pronombres, Objetos,
  Personas).
- **FR-004**: El sistema MUST ofrecer dos modos de visualización: tarjetas (cards) y lista
  (tabla).
- **FR-005**: El sistema MUST mostrar un estado vacío cuando la búsqueda no tiene
  resultados.
- **FR-006**: El sistema MUST mostrar el conteo total de términos y la distribución
  semántica por categoría (gráfico de barras o similar).
- **FR-007**: El sistema MUST incluir la sección "Gramática" con los 7 bloques del
  prototipo HTML, preservando todos los ejemplos.
- **FR-008**: El sistema MUST incluir la sección "Números" con el contador interactivo,
  la fórmula constructiva y la tabla de clasificadores numerales.
- **FR-009**: El sistema MUST incluir la sección "Práctica" con quiz de opción múltiple
  (1 correcta + 3 distractores aleatorios) y puntaje acumulado por sesión.
- **FR-010**: El sistema MUST incluir la sección "El Significado de Muchik" con contenido
  etimológico e histórico completo del prototipo HTML.
- **FR-011**: El sistema MUST incluir la sección "Créditos" con fuentes históricas y
  modernas documentadas (De la Carrera 1644, Montjoy 1865, Middendorf 1892, Brüning 1924,
  Villarreal 1921, Cerrón-Palomino, Eloranta 2025, USS, UNT, Instituto Elim).
- **FR-012**: El sistema MUST incluir la sección "Contacto" con email cr.htorres@gmail.com.
- **FR-013**: El sistema MUST estar disponible como app nativa en Web (WASM), Desktop
  (JVM), Android e iOS con funcionalidad equivalente.
- **FR-014**: El sistema MUST almacenar el corpus completo localmente para uso sin
  conexión.
- **FR-015**: El sistema MUST usar búsqueda FTS que soporte correctamente los caracteres
  especiales del idioma Muchik (æ, ç, ñ, xll, tzh).
- **FR-016**: El sistema MUST implementar la siguiente estrategia de sincronización
  unidireccional (servidor → app):
  - **Primera apertura**: mostrar una pantalla de carga (loading) y descargar el corpus
    completo desde la fuente remota antes de dar acceso al diccionario.
  - **Aperturas posteriores**: usar exclusivamente los datos locales; no realizar ninguna
    llamada automática a la red.
  - **Actualización voluntaria**: ofrecer un botón visible "Actualizar fuentes" que
    el usuario puede pulsar cuando desee para descargar las entradas nuevas o corregidas
    disponibles en la fuente remota.
- **FR-017**: La identidad visual MUST ser fiel al prototipo: paleta tierra (arcilla
  #C06C47, arena #F3E5AB, dorado #D4AF37, océano #4B8F8C), tipografía serif para títulos
  y sans-serif para cuerpo.

### Key Entities

- **EntradaDiccionario**: término Muchik, traducción española, categoría, emoji,
  notas etimológicas, fuente de referencia, fecha de última actualización.
- **Categoría**: identificador, nombre en español, conteo de entradas asociadas.
- **EntradaNúmero**: valor numérico, nombre en Muchik.
- **ClasificadorNumeral**: nombre (Pong, Ssop, etc.), descripción de uso, rango
  (decenas/centenas/millares/pares).
- **SesiónQuiz**: puntaje de la sesión actual (efímero, no persiste entre aperturas).

---

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 100% de las secciones y funcionalidades del prototipo HTML están
  disponibles en la app nativa (ninguna vista ni función perdida).
- **SC-002**: La búsqueda devuelve resultados en menos de 300 ms para el corpus completo
  (410+ entradas) en todos los dispositivos objetivo.
- **SC-003**: La app funciona completamente sin conexión una vez descargado el corpus
  inicial (0 errores de funcionalidad offline).
- **SC-004**: Los 410+ términos verificados están disponibles desde el primer día de
  lanzamiento de cada plataforma.
- **SC-005**: La app está disponible en las 4 plataformas objetivo al completar todas
  las etapas del proyecto.
- **SC-006**: Los caracteres especiales Muchik (æ, ç, xll, tzh) se buscan y muestran
  sin corrupción en todas las plataformas.
- **SC-007**: La identidad visual (colores, jerarquía tipográfica, estilo de tarjetas)
  es fidelmente reproducida en todas las plataformas.

---

## Assumptions

- El corpus inicial (410+ términos) se extrae directamente de `prototype/muchik_old.html`
  y se estructura como datos de seed para la primera versión.
- La interfaz es 100% en español; el idioma Muchik aparece solo como contenido del
  diccionario, no como idioma de la UI.
- El orden de implementación es: Web (WASM) → Desktop (JVM) → Android → iOS.
- La paleta de colores y la jerarquía tipográfica del prototipo se trasladan a Compose
  Multiplatform como design tokens reutilizables.
- La sincronización con la fuente remota es unidireccional (solo descarga); los usuarios
  no editan el corpus desde la app.
- Las opciones del quiz se generan en memoria desde el corpus local; no requieren
  persistencia propia.
- Las secciones "Créditos" y "Contacto" son vistas estáticas sin lógica de negocio.
- Los tests cubren: ViewModels/UseCases (unitarios) y flujos principales de búsqueda y
  navegación (integración/UI).
