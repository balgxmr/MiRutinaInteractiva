package com.example.mirutinainteractiva.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mirutinainteractiva.data.models.Routine
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun RoutineCard(
    routine: Routine,
    onClick: () -> Unit,
    onDelete: (Routine) -> Unit
) {
    var openDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        onClick = onClick
    ) {
        Column(Modifier.padding(16.dp)) {
            routine.imageRes?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = routine.title,
                    modifier = Modifier.fillMaxWidth().height(120.dp)
                )
                Spacer(Modifier.height(8.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(Modifier.weight(1f)) {
                    Text(routine.title, style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(4.dp))
                    Text(routine.description, style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(8.dp))
                    AssistChip(onClick = {}, label = { Text("Dificultad: ${routine.difficulty}") })
                }
                IconButton(onClick = { openDialog = true }) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar rutina", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }

    // 🔹 Pop-up confirmación
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