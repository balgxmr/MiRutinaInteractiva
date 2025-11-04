package com.example.mirutinainteractiva.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mirutinainteractiva.data.models.Routine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineExecutionScreen(
    routine: Routine,
    onFinish: () -> Unit,
    onDelete: (Routine) -> Unit
) {
    var openDialog by remember { mutableStateOf(false) }

    val steps = listOf("Preparar materiales", "Realizar actividad principal", "Guardar y limpiar")
    val checkedStates = remember { mutableStateListOf(*Array(steps.size) { false }) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(routine.title) },
                actions = {
                    IconButton(onClick = { openDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar rutina")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(routine.description, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Dificultad: ${routine.difficulty}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))

            Text("Pasos de la rutina", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                itemsIndexed(steps) { index, step ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = checkedStates[index],
                            onCheckedChange = { checkedStates[index] = it }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(step, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onFinish() },
                modifier = Modifier.fillMaxWidth(),
                enabled = checkedStates.all { it }
            ) {
                Text("Finalizar rutina")
            }
        }
    }

    // 🔹 Pop-up de confirmación
    if (openDialog) {
        AlertDialog(
            onDismissRequest = { openDialog = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Seguro que deseas borrar la rutina actual?") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete(routine)
                    openDialog = false
                }) {
                    Text("Sí, borrar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { openDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}