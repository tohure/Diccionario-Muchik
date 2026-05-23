package dev.tohure.muchik_dictionary.feature.numbers.domain.model

data class NumberEntry(
    val value: Int?,
    val muchikTerm: String,
    val spanishTranslation: String,
) {
    companion object {
        val all: List<NumberEntry>
            get() =
                listOf(
                    NumberEntry(1, "Onæc", "Uno"),
                    NumberEntry(2, "Atput", "Dos"),
                    NumberEntry(3, "Çopæt", "Tres"),
                    NumberEntry(4, "Nopæt", "Cuatro"),
                    NumberEntry(5, "Exllmætzh", "Cinco"),
                    NumberEntry(6, "Tzhaxlltzha", "Seis"),
                    NumberEntry(7, "Ñite", "Siete"),
                    NumberEntry(8, "Langæss / Janes", "Ocho"),
                    NumberEntry(9, "Tap", "Nueve"),
                    NumberEntry(10, "Çiæcy / Napong", "Diez"),
                    NumberEntry(11, "Napong Allo Onæc", "Once"),
                    NumberEntry(12, "Napon Allo Aput", "Doce"),
                    NumberEntry(14, "Napong Allo Nopet", "Catorce"),
                    NumberEntry(15, "Napong Allo Exllmætzh", "Quince"),
                    NumberEntry(16, "Napong Allo Tzhaxlltzha", "Dieciséis"),
                    NumberEntry(17, "Napong Allo Ñite", "Diecisiete"),
                    NumberEntry(18, "Napong Allo Langæss", "Dieciocho"),
                    NumberEntry(19, "Napong Allo Tap", "Diecinueve"),
                    NumberEntry(20, "Pachon / Pacpong", "Veinte"),
                    NumberEntry(21, "Pacpong Allo Onæc", "Veintiuno"),
                    NumberEntry(22, "Pacpong Allo Atput", "Veintidós"),
                    NumberEntry(23, "Pacpong Allo Çopæt", "Veintitrés"),
                    NumberEntry(30, "Zochon / Çocpong", "Treinta"),
                    NumberEntry(40, "Nocpong", "Cuarenta"),
                    NumberEntry(50, "Exllmætzhpong", "Cincuenta"),
                    NumberEntry(60, "Tzhaxlltzhapong", "Sesenta"),
                    NumberEntry(100, "Napacjac / Onæcpalæc", "Cien"),
                    NumberEntry(1000, "Naponpajac / Onæccunô", "Mil"),
                    NumberEntry(null, "Nacíofec", "Muchos / Bastante"),
                    NumberEntry(null, "Chăm", "Abundante"),
                )

        val countable: List<NumberEntry>
            get() = all.filter { it.value != null }
    }
}
