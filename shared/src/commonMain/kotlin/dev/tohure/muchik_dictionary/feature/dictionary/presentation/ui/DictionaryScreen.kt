package dev.tohure.muchik_dictionary.feature.dictionary.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.tohure.muchik_dictionary.core.design.Clay
import dev.tohure.muchik_dictionary.core.design.DarkClay
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

    val contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)

    if (state.viewMode == DictionaryViewMode.LIST) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(12.dp)
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
            contentPadding = contentPadding,
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
            .padding(vertical = 12.dp),
    ) {
        if (maxWidth > 650.dp) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StatsCard(totalCount = totalCount, modifier = Modifier.width(260.dp))
                DonutChartCard(categoryCounts = categoryCounts, modifier = Modifier.weight(1f))
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
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
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            placeholder = {
                Text(
                    text = "Buscar en Muchik o Español...",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                )
            },
            singleLine = true,
            shape = CircleShape,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            ),
            textStyle = MaterialTheme.typography.bodyMedium
        )
        CategoryDropdown(
            selectedCategory = selectedCategory,
            onCategorySelected = onCategorySelected,
            modifier = Modifier.width(IntrinsicSize.Min),
        )
    }
}

@Composable
private fun ViewModeRow(viewMode: DictionaryViewMode, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Visualización:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.weight(1f))
        ViewModeToggle(viewMode = viewMode, onToggle = onToggle)
    }
}

@Composable
private fun StatsCard(totalCount: Int, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "TOTAL ENTRADAS",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.padding(top = 12.dp))
            Text(
                text = totalCount.toString(),
                style = MaterialTheme.typography.displayLarge,
                color = DarkClay,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )
            Text(
                text = "términos verificados",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun DonutChartCard(categoryCounts: Map<String, Int>, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Distribución Semántica",
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
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                shape = CircleShape
            )
            .padding(4.dp),
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(
                    color = if (isCards) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                    shape = CircleShape,
                )
                .clickable { if (!isCards) onToggle() }
                .padding(horizontal = 16.dp, vertical = 6.dp),
        ) {
            Text(
                text = "Tarjetas",
                style = MaterialTheme.typography.labelMedium,
                color = if (isCards) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = if (isCards) FontWeight.SemiBold else FontWeight.Normal,
            )
        }
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(
                    color = if (!isCards) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                    shape = CircleShape,
                )
                .clickable { if (isCards) onToggle() }
                .padding(horizontal = 16.dp, vertical = 6.dp),
        ) {
            Text(
                text = "Lista",
                style = MaterialTheme.typography.labelMedium,
                color = if (!isCards) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = if (!isCards) FontWeight.SemiBold else FontWeight.Normal,
            )
        }
    }
}
