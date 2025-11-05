package com.example.mirutinainteractiva.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mirutinainteractiva.data.local.SubtaskEntity
import com.example.mirutinainteractiva.data.models.Routine
import com.example.mirutinainteractiva.ui.viewmodel.RoutineViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineExecutionScreen(
    routine: Routine,
    routineViewModel: RoutineViewModel,
    onFinish: () -> Unit,
    onDelete: (Routine) -> Unit
) {
    var openDialog by remember { mutableStateOf(false) }
    var newSubtaskTitle by remember { mutableStateOf("") }

    // Obtenemos la rutina con sus subtareas desde el ViewModel
    val routineWithSubtasks by routineViewModel
        .getRoutineWithSubtasks(routine.id)
        .collectAsState(initial = null)

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

            Text("Subtareas", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            // Campo para añadir nueva subtarea
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = newSubtaskTitle,
                    onValueChange = { newSubtaskTitle = it },
                    label = { Text("Nueva subtarea") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (newSubtaskTitle.isNotBlank()) {
                            routineViewModel.addSubtask(routine.id, newSubtaskTitle)
                            newSubtaskTitle = ""
                        }
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar subtarea")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de subtareas dinámicas
            LazyColumn(modifier = Modifier.weight(1f)) {
                routineWithSubtasks?.let { data ->
                    items(data.subtasks) { subtask ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = subtask.completed,
                                onCheckedChange = {
                                    routineViewModel.toggleSubtask(subtask)
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(subtask.title, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón finalizar rutina
            Button(
                onClick = { onFinish() },
                modifier = Modifier.fillMaxWidth(),
                enabled = routineWithSubtasks?.subtasks?.all { it.completed } == true
            ) {
                Text("Finalizar rutina")
            }
        }
    }

    // Pop-up de confirmación de borrado
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