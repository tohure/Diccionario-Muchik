package dev.tohure.muchik_dictionary.feature.dictionary.domain.model

enum class WordCategory(val displayName: String) {
    ALL("Todas las Categorías"),
    EDUCACION("Educación"),
    EXPRESIONES("Expresiones"),
    TIEMPO("Tiempo"),
    CONCEPTOS("Conceptos"),
    NATURALEZA("Naturaleza"),
    TOPONIMIA("Toponimia"),
    PRONOMBRES("Pronombres"),
    PERSONAS("Personas"),
    FAMILIA("Familia"),
    CUERPO("Cuerpo"),
    ANIMALES("Animales"),
    ALIMENTOS("Alimentos"),
    OBJETOS("Objetos"),
    ADJETIVOS("Adjetivos"),
    VERBOS("Verbos"),
    NUMEROS("Números");

    companion object {
        fun fromString(value: String): WordCategory =
            entries.firstOrNull { it.displayName == value } ?: CONCEPTOS
    }
}
