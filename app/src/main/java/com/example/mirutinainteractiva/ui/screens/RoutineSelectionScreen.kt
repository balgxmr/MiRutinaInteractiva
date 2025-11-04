package com.example.mirutinainteractiva.ui.screens

import androidx.compose.foundation.layout.*
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
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var difficulty by remember { mutableStateOf("Fácil") }
    var expanded by remember { mutableStateOf(false) }

    // Estados de error
    var titleError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Crear nueva rutina",
                style = MaterialTheme.typography.headlineMedium,
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
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
                    listOf("Fácil", "Medio", "Difícil").forEach { option ->
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

            Button(
                onClick = {
                    // Validación
                    titleError = title.isBlank()
                    descriptionError = description.isBlank()

                    if (!titleError && !descriptionError) {
                        routineViewModel.addRoutine(
                            title = title,
                            description = description,
                            difficulty = difficulty
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