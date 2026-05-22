package dev.tohure.muchik_dictionary.feature.sync.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.tohure.muchik_dictionary.core.design.DarkClay
import dev.tohure.muchik_dictionary.feature.sync.presentation.state.SyncUiState
import dev.tohure.muchik_dictionary.feature.sync.presentation.viewmodel.SyncViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SyncScreen(
    onSyncComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: SyncViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        if (uiState is SyncUiState.Done) onSyncComplete()
    }

    when (val state = uiState) {
        SyncUiState.Loading -> SyncLoadingContent(modifier)
        SyncUiState.Done -> Unit
        is SyncUiState.Error -> SyncErrorContent(
            message = state.message,
            onRetry = viewModel::retry,
            modifier = modifier,
        )
    }
}

@Composable
private fun SyncLoadingContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Ejep Muchik",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = DarkClay,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Diccionario Mochica",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(32.dp))
        CircularProgressIndicator(
            modifier = Modifier.size(40.dp),
            color = DarkClay,
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Cargando corpus léxico...",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun SyncErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "No se pudo cargar el diccionario",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}
