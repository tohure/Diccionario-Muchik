package dev.tohure.muchik_dictionary.core.design

import androidx.compose.ui.graphics.Color

val categoryColorMap: Map<String, Color> = mapOf(
    "Educación"  to Color(0xFF5C3317),
    "Objetos"    to Color(0xFFDAA520),
    "Personas"   to Color(0xFF2E8B57),
    "Conceptos"  to Color(0xFFA0522D),
    "Expresiones" to Color(0xFFD4B896),
    "Tiempo"     to Color(0xFF888888),
    "Naturaleza" to Color(0xFFF4A460),
    "Toponimia"  to Color(0xFF6B8E23),
    "Pronombres" to Color(0xFF4169E1),
    "Familia"    to Color(0xFFDB7093),
    "Cuerpo"     to Color(0xFF9370DB),
    "Animales"   to Color(0xFFCD5C5C),
    "Alimentos"  to Color(0xFF20B2AA),
    "Adjetivos"  to Color(0xFF4682B4),
    "Verbos"     to Color(0xFFC06C47),
    "Números"    to Color(0xFFD4AF37),
)

fun categoryColor(category: String): Color =
    categoryColorMap[category] ?: Color(0xFFC06C47)
