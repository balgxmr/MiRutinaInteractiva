package com.example.mirutinainteractiva.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mirutinainteractiva.ui.viewmodel.RoutineViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineSelectionScreen(
    routineViewModel: RoutineViewModel,
    onRoutineCreated: () -> Unit
) {
    // Mostrar rutinas preestablecidas primero
    val predefinedRoutines = listOf(
        PredefinedRoutine(
            title = "Rutina de la mañana",
            description = "Despiértate, tiende la cama, lávate los dientes y desayuna.",
            subtasks = listOf("Despertarse", "Tender la cama", "Lavarse los dientes", "Desayunar", "Vestirse")
        ),
        PredefinedRoutine(
            title = "Hora de dormir",
            description = "Prepara todo para dormir bien.",
            subtasks = listOf("Ponerse pijama", "Cepillarse los dientes", "Leer un cuento")
        ),
        PredefinedRoutine(
            title = "Limpieza del cuarto",
            description = "Organiza tu habitación y deja todo limpio.",
            subtasks = listOf("Guardar juguetes", "Tender la cama", "Limpiar escritorio", "Barrer", "Pasar paño", "Botar basura", "Ventilar la habitación")
        )
    )

    var showCustomRoutineForm by remember { mutableStateOf(false) }

    // Estados del formulario (para la rutina personalizada)
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var difficulty by remember { mutableStateOf("Fácil") }
    var expanded by remember { mutableStateOf(false) }

    var titleError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }

    var newSubtaskTitle by remember { mutableStateOf("") }
    val subtasks = remember { mutableStateListOf<String>() }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Selecciona una rutina preestablecida",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Mostrar rutinas predefinidas
            items(predefinedRoutines) { routine ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            val difficulty = when {
                                routine.subtasks.size <= 3 -> "Fácil"
                                routine.subtasks.size <= 5 -> "Intermedio"
                                else -> "Difícil"
                            }
                            routineViewModel.addRoutineWithSubtasks(
                                title = routine.title,
                                description = routine.description,
                                difficulty = difficulty,
                                subtasks = routine.subtasks
                            )
                            onRoutineCreated()
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(routine.title, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(routine.description, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tareas: ${routine.subtasks.size}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }

            // Botón para mostrar el formulario personalizado
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { showCustomRoutineForm = !showCustomRoutineForm },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar rutina personalizada")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (showCustomRoutineForm) "Ocultar formulario" else "Crear rutina personalizada")
                }
            }

            // Formulario secundario (solo se muestra si el usuario lo abre)
            if (showCustomRoutineForm) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Crear nueva rutina personalizada",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo título
                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                            if (it.isNotBlank()) titleError = false
                        },
                        label = { Text("Título") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = titleError
                    )
                    if (titleError) {
                        Text(
                            text = "El título es obligatorio",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Campo descripción
                    OutlinedTextField(
                        value = description,
                        onValueChange = {
                            description = it
                            if (it.isNotBlank()) descriptionError = false
                        },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = descriptionError
                    )
                    if (descriptionError) {
                        Text(
                            text = "La descripción es obligatoria",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Selector de dificultad
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = difficulty,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Dificultad") },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            listOf("Fácil", "Intermedio", "Difícil").forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        difficulty = option
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Subtareas
                    Text("Subtareas", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))

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
                                    subtasks.add(newSubtaskTitle)
                                    newSubtaskTitle = ""
                                }
                            }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Agregar subtarea")
                        }
                    }

                    subtasks.forEach { subtask ->
                        Text("• $subtask", style = MaterialTheme.typography.bodyLarge)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            titleError = title.isBlank()
                            descriptionError = description.isBlank()

                            if (!titleError && !descriptionError) {
                                routineViewModel.addRoutineWithSubtasks(
                                    title = title,
                                    description = description,
                                    difficulty = difficulty,
                                    subtasks = subtasks.toList()
                                )
                                onRoutineCreated()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Guardar rutina")
                    }
                }
            }
        }
    }
}

data class PredefinedRoutine(
    val title: String,
    val description: String,
    val subtasks: List<String>
)
