package com.fung.entity

data class SpecialSymbol(
    val style: String, val start: Int, val end: Int
) {
    val type: StyleType
        get() = getStyleType(style)
}

private fun getStyleType(type: String) = when (type) {
    "UNDERLINE" -> StyleType.UnderLine
    "DOT" -> StyleType.Dot
    "STRONG" -> StyleType.Strong
    else -> StyleType.None
}

data class SpecialStyle(
    val stemSpecialSymbol: List<SpecialSymbol>
)

sealed class StyleType {
    object UnderLine : StyleType()
    object Dot : StyleType()
    object Strong : StyleType()
    object None : StyleType()
}
