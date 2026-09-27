package com.ifsp.crossfitwod.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
fun HomeScreen(
    viewModel: WodViewModel,
    onAddWod: () -> Unit,
    onWodClick: (String) -> Unit
) {
    val wods by viewModel.wods.collectAsStateWithLifecycle()
    var selectedType by remember { mutableStateOf("Todos") }
    val tipos = listOf("Todos", "AMRAP", "EMOM", "For Time", "RFT", "Benchmark", "Outro")

    val filteredWods = if (selectedType == "Todos") {
        wods
    } else {
        wods.filter { it.tipo == selectedType }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CrossFit WOD Tracker") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddWod) {
                Icon(Icons.Default.Add, contentDescription = "Add WOD")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            ScrollableTabRow(
                selectedTabIndex = tipos.indexOf(selectedType).coerceAtLeast(0),
                edgePadding = 16.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                divider = {}
            ) {
                tipos.forEach { tipo ->
                    FilterChip(
                        selected = selectedType == tipo,
                        onClick = { selectedType = tipo },
                        label = { Text(tipo) },
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredWods) { wod ->
                    WodCard(wod = wod, onClick = { onWodClick(wod.id) })
                }
                
                if (filteredWods.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Nenhum WOD encontrado.", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WodCard(
    wod: Wod,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = wod.nome,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = wod.tipo,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = wod.descricao,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2
            )
            
            wod.resultados.lastOrNull()?.let { lastResult ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Melhor: ${lastResult.score}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
