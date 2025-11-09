package com.example.mirutinainteractiva.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mirutinainteractiva.data.local.RoutineEntity
import com.example.mirutinainteractiva.ui.viewmodel.RoutineViewModel

@Composable
fun RoutinePickerScreen(
    routineViewModel: RoutineViewModel,
    onRoutineSelected: (RoutineEntity) -> Unit,
    onCreateRoutineClick: () -> Unit
) {
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        item {
            Text(
                text = "Selecciona una rutina preestablecida",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

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
                        // Navegar a ejecución
                        onRoutineSelected(
                            RoutineEntity(
                                title = routine.title,
                                description = routine.description,
                                difficulty = difficulty,
                                isPredefined = true,
                                timeOfDay = "Mañana"
                            )
                        )
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

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onCreateRoutineClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear rutina personalizada")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Crear rutina personalizada")
            }
        }
    }
}

data class PredefinedRoutine(
    val title: String,
    val description: String,
    val subtasks: List<String>
)