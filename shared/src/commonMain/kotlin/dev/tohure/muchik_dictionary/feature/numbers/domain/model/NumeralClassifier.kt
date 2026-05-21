package dev.tohure.muchik_dictionary.feature.numbers.domain.model

enum class ClassifierGroup(val label: String, val emoji: String) {
    DECENAS_GENERALES("Para Decenas Generales", "👥"),
    CENTENAS_MILLARES("Para Centenas y Millares", "💯"),
    SISTEMA_DUAL("Sistema Dual (Pares)", "🦆"),
}

data class NumeralClassifier(
    val muchikTerm: String,
    val shortLabel: String,   // label corto para la lista de referencia (campo "spanish" del HTML)
    val description: String,  // descripción completa para la tabla de clasificadores
    val group: ClassifierGroup,
) {
    companion object {
        val all: List<NumeralClassifier>
            get() = listOf(
                NumeralClassifier("Pong", "Clasificador: Personas / Animales", "Partícula para contar seres vivos u objetos generales", ClassifierGroup.DECENAS_GENERALES),
                NumeralClassifier("Ssop", "Clasificador: Frutas / Monedas", "Partícula para contar objetos pequeños y redondos, o años", ClassifierGroup.DECENAS_GENERALES),
                NumeralClassifier("Cuo quixll", "Clasificador: Mazorcas", "Específico para agrupar mazorcas (choclos)", ClassifierGroup.DECENAS_GENERALES),
                NumeralClassifier("Cæss", "Clasificador: Días", "Exclusivo para contar jornadas (días)", ClassifierGroup.DECENAS_GENERALES),
                NumeralClassifier("Palæc", "Clasificador: Centenas", "Para la suma de cienes en general", ClassifierGroup.CENTENAS_MILLARES),
                NumeralClassifier("Chiæng", "Clasificador: Centenas frutales", "Para cienes de vegetales o frutos", ClassifierGroup.CENTENAS_MILLARES),
                NumeralClassifier("Cunô", "Clasificador: Millares", "El clasificador de todos los millares", ClassifierGroup.CENTENAS_MILLARES),
                NumeralClassifier("Luc", "Clasificador Dual: Redondos", "Para contar pares de cosas redondas (huevos, platos)", ClassifierGroup.SISTEMA_DUAL),
                NumeralClassifier("Felæp", "Clasificador Dual: Aves / Potos", "Para contar pares de aves o vasijas (potos)", ClassifierGroup.SISTEMA_DUAL),
            )
    }
}
