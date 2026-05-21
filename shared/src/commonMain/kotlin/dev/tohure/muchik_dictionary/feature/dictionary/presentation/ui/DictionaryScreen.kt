package dev.tohure.muchik_dictionary.feature.dictionary.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.tohure.muchik_dictionary.core.design.Clay
import dev.tohure.muchik_dictionary.core.design.ClayLight
import dev.tohure.muchik_dictionary.core.design.DarkClay
import dev.tohure.muchik_dictionary.core.design.Sand
import dev.tohure.muchik_dictionary.feature.dictionary.domain.model.WordCategory
import dev.tohure.muchik_dictionary.feature.dictionary.presentation.state.DictionaryViewMode
import dev.tohure.muchik_dictionary.feature.dictionary.presentation.viewmodel.DictionaryViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DictionaryScreen(viewModel: DictionaryViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsState()

    val showDashboard = state.query.isBlank() &&
        state.selectedCategory == WordCategory.ALL &&
        state.categoryCounts.isNotEmpty()

    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = Clay)
        }
        return
    }

    if (state.viewMode == DictionaryViewMode.LIST) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            if (showDashboard) {
                item { DashboardSection(state.totalCount, state.categoryCounts) }
            }
            item {
                SearchAndFilterRow(
                    query = state.query,
                    selectedCategory = state.selectedCategory,
                    onQueryChange = viewModel::onQueryChange,
                    onCategorySelected = viewModel::onCategorySelected,
                )
            }
            item {
                ViewModeRow(viewMode = state.viewMode, onToggle = viewModel::onViewModeToggle)
            }
            if (state.entries.isEmpty()) {
                item { EmptyStateView(query = state.query) }
            } else {
                stickyHeader { WordListHeader() }
                items(state.entries, key = { it.id }) { entry ->
                    WordListItem(entry = entry)
                }
            }
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 280.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (showDashboard) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    DashboardSection(state.totalCount, state.categoryCounts)
                }
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                SearchAndFilterRow(
                    query = state.query,
                    selectedCategory = state.selectedCategory,
                    onQueryChange = viewModel::onQueryChange,
                    onCategorySelected = viewModel::onCategorySelected,
                )
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                ViewModeRow(viewMode = state.viewMode, onToggle = viewModel::onViewModeToggle)
            }
            if (state.entries.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    EmptyStateView(query = state.query)
                }
            } else {
                items(state.entries, key = { it.id }) { entry ->
                    WordCard(entry = entry)
                }
            }
        }
    }
}

@Composable
private fun DashboardSection(totalCount: Int, categoryCounts: Map<String, Int>) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        if (maxWidth > 500.dp) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top,
            ) {
                StatsCard(totalCount = totalCount, modifier = Modifier.width(200.dp))
                DonutChartCard(categoryCounts = categoryCounts, modifier = Modifier.weight(1f))
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                StatsCard(totalCount = totalCount, modifier = Modifier.fillMaxWidth())
                DonutChartCard(categoryCounts = categoryCounts, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun SearchAndFilterRow(
    query: String,
    selectedCategory: WordCategory,
    onQueryChange: (String) -> Unit,
    onCategorySelected: (WordCategory) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            placeholder = {
                Text(
                    text = "Buscar en Español, Muchik o buscar notas etimológicas...",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                )
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Clay,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            ),
        )
        CategoryDropdown(
            selectedCategory = selectedCategory,
            onCategorySelected = onCategorySelected,
            modifier = Modifier.width(180.dp),
        )
    }
}

@Composable
private fun ViewModeRow(viewMode: DictionaryViewMode, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Visualización de resultados:",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.weight(1f))
        ViewModeToggle(viewMode = viewMode, onToggle = onToggle)
    }
}

@Composable
private fun StatsCard(totalCount: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, ClayLight),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "TOTAL ENTRADAS",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold,
            )
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.padding(top = 8.dp),
            ) {
                Text(
                    text = totalCount.toString(),
                    style = MaterialTheme.typography.displayMedium,
                    color = DarkClay,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = " términos\nverificados",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp, start = 4.dp),
                )
            }
        }
    }
}

@Composable
private fun DonutChartCard(categoryCounts: Map<String, Int>, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Distribución Semántica del Léxico",
                style = MaterialTheme.typography.titleSmall,
                color = DarkClay,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp),
            )
            DonutChart(categoryCounts = categoryCounts)
        }
    }
}

@Composable
private fun ViewModeToggle(viewMode: DictionaryViewMode, onToggle: () -> Unit) {
    val isCards = viewMode == DictionaryViewMode.CARDS

    Row(
        modifier = Modifier.border(1.dp, ClayLight, RoundedCornerShape(20.dp)),
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (isCards) Sand else Color.Transparent,
                    shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp),
                )
                .clickable { if (!isCards) onToggle() }
                .padding(horizontal = 12.dp, vertical = 6.dp),
        ) {
            Text(
                text = "Tarjetas",
                style = MaterialTheme.typography.labelMedium,
                color = if (isCards) DarkClay else MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = if (isCards) FontWeight.SemiBold else FontWeight.Normal,
            )
        }
        Box(
            modifier = Modifier
                .background(
                    color = if (!isCards) Sand else Color.Transparent,
                    shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp),
                )
                .clickable { if (isCards) onToggle() }
                .padding(horizontal = 12.dp, vertical = 6.dp),
        ) {
            Text(
                text = "Lista",
                style = MaterialTheme.typography.labelMedium,
                color = if (!isCards) DarkClay else MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = if (!isCards) FontWeight.SemiBold else FontWeight.Normal,
            )
        }
    }
}
