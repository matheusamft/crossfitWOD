package com.ifsp.crossfitwod.ui.wod

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ifsp.crossfitwod.data.model.Exercicio
import com.ifsp.crossfitwod.data.model.Wod
import com.ifsp.crossfitwod.viewmodel.WodViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WodFormScreen(
    viewModel: WodViewModel,
    wodId: String?,
    onNavigateBack: () -> Unit
) {
    val wods by viewModel.wods.collectAsStateWithLifecycle()
    val editingWod = remember(wodId, wods) {
        wods.find { it.id == wodId }
    }

    var nome by rememberSaveable { mutableStateOf(editingWod?.nome ?: "") }
    var tipo by rememberSaveable { mutableStateOf(editingWod?.tipo ?: "AMRAP") }
    var descricao by rememberSaveable { mutableStateOf(editingWod?.descricao ?: "") }
    var tempoMinutos by rememberSaveable { mutableStateOf(editingWod?.tempoMinutos?.toString() ?: "") }
    
    val exercicios = remember {
        mutableStateListOf<Exercicio>().apply {
            addAll(editingWod?.exercicios ?: emptyList())
        }
    }

    var expanded by remember { mutableStateOf(false) }
    val tipos = listOf("AMRAP", "EMOM", "For Time", "RFT", "Benchmark", "Outro")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (wodId == null) "Novo WOD" else "Editar WOD") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome do WOD") },
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = tipo,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable, true).fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    tipos.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                tipo = selectionOption
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            OutlinedTextField(
                value = tempoMinutos,
                onValueChange = { tempoMinutos = it },
                label = { Text("Tempo (minutos)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Exercícios", style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = { exercicios.add(Exercicio("", "")) }) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar Exercício")
                }
            }

            exercicios.forEachIndexed { index, exercicio ->
                ExerciseFormItem(
                    exercicio = exercicio,
                    onUpdate = { updated -> exercicios[index] = updated },
                    onDelete = { exercicios.removeAt(index) }
                )
            }

            Button(
                onClick = {
                    if (nome.isBlank()) return@Button
                    
                    val newWod = Wod(
                        id = wodId ?: UUID.randomUUID().toString(),
                        nome = nome,
                        tipo = tipo,
                        descricao = descricao,
                        tempoMinutos = tempoMinutos.toIntOrNull(),
                        exercicios = exercicios.toList(),
                        resultados = editingWod?.resultados ?: emptyList()
                    )
                    
                    if (wodId == null) {
                        viewModel.addWod(newWod)
                    } else {
                        viewModel.updateWod(newWod)
                    }
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("SALVAR WOD")
            }
        }
    }
}

@Composable
fun ExerciseFormItem(
    exercicio: Exercicio,
    onUpdate: (Exercicio) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = exercicio.nome,
                    onValueChange = { onUpdate(exercicio.copy(nome = it)) },
                    label = { Text("Exercício") },
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Remover")
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = exercicio.repeticoes,
                    onValueChange = { onUpdate(exercicio.copy(repeticoes = it)) },
                    label = { Text("Reps") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = exercicio.carga?.toString() ?: "",
                    onValueChange = { onUpdate(exercicio.copy(carga = it.toDoubleOrNull())) },
                    label = { Text("Carga (kg)") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
    }
}
