package com.ifsp.crossfitwod.ui.result

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ifsp.crossfitwod.data.model.Resultado
import com.ifsp.crossfitwod.viewmodel.WodViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    viewModel: WodViewModel,
    wodId: String,
    onNavigateBack: () -> Unit
) {
    var score by rememberSaveable { mutableStateOf("") }
    var observacao by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Resultado") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = score,
                onValueChange = { score = it },
                label = { Text("Resultado / Score") },
                placeholder = { Text("Ex: 06:42 ou 12 + 7") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = observacao,
                onValueChange = { observacao = it },
                label = { Text("Observação") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Button(
                onClick = {
                    if (score.isBlank()) return@Button
                    
                    val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                    val novoResultado = Resultado(
                        id = UUID.randomUUID().toString(),
                        data = date,
                        score = score,
                        observacao = observacao
                    )
                    
                    viewModel.addResult(wodId, novoResultado)
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("REGISTRAR RESULTADO")
            }
        }
    }
}
