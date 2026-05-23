package dev.tohure.muchik_dictionary.feature.numbers.presentation.state

import dev.tohure.muchik_dictionary.feature.numbers.domain.model.NumberEntry

data class NumbersUiState(
    val currentIndex: Int = 0,
    val entries: List<NumberEntry> = NumberEntry.countable,
) {
    val currentEntry: NumberEntry get() = entries[currentIndex]
    val canDecrement: Boolean get() = currentIndex > 0
    val canIncrement: Boolean get() = currentIndex < entries.lastIndex
}
