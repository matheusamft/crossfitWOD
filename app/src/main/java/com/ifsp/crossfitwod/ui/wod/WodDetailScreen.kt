package com.ifsp.crossfitwod.ui.wod

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ifsp.crossfitwod.data.model.Wod
import com.ifsp.crossfitwod.viewmodel.WodViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WodDetailScreen(
    viewModel: WodViewModel,
    wodId: String,
    onNavigateBack: () -> Unit,
    onEditWod: (String) -> Unit,
    onRegisterResult: (String) -> Unit
) {
    val wods by viewModel.wods.collectAsStateWithLifecycle()
    val wod = remember(wodId, wods) {
        wods.find { it.id == wodId }
    }

    var showDeleteDialog by remember { mutableStateOf(false) }

    if (wod == null) {
        LaunchedEffect(Unit) { onNavigateBack() }
        return
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Excluir WOD") },
            text = { Text("Deseja realmente excluir este WOD?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteWod(wod.id)
                    showDeleteDialog = false
                    onNavigateBack()
                }) {
                    Text("EXCLUIR")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("CANCELAR")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do WOD") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onEditWod(wod.id) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(wod.nome, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text(wod.tipo, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                wod.tempoMinutos?.let {
                    Text("$it minutos", style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(wod.descricao, style = MaterialTheme.typography.bodyLarge)
            }

            item {
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))
                Text("Exercícios", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }

            items(wod.exercicios) { exercicio ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(exercicio.nome, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("Repetições: ${exercicio.repeticoes}", style = MaterialTheme.typography.bodyMedium)
                        exercicio.carga?.let {
                            Text("Carga: $it kg", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Histórico", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Button(onClick = { onRegisterResult(wod.id) }) {
                        Text("REGISTRAR RESULTADO")
                    }
                }
            }

            if (wod.resultados.isEmpty()) {
                item {
                    Text("Nenhum resultado registrado.", style = MaterialTheme.typography.bodyMedium)
                }
            } else {
                items(wod.resultados.reversed()) { resultado ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(resultado.score, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                Text(resultado.data, style = MaterialTheme.typography.bodySmall)
                            }
                            if (resultado.observacao.isNotBlank()) {
                                Text(resultado.observacao, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}
