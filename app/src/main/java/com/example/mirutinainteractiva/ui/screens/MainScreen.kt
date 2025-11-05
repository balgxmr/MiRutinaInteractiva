package com.example.mirutinainteractiva.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mirutinainteractiva.data.models.Routine
import com.example.mirutinainteractiva.ui.components.RoutineCard

@Composable
fun MainScreen(
    routines: List<Routine>,
    completedRoutines: List<Routine>,
    onStartClick: () -> Unit,
    onRoutineClick: (Routine) -> Unit,
    onDeleteRoutine: (Routine) -> Unit,
    onRestoreRoutine: (Routine) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var recentlyDeletedRoutine by remember { mutableStateOf<Routine?>(null) }

    // Mostrar SnackBar cuando se borra una rutina
    LaunchedEffect(recentlyDeletedRoutine) {
        recentlyDeletedRoutine?.let { routine ->
            val result = snackbarHostState.showSnackbar(
                message = "Rutina \"${routine.title}\" eliminada",
                actionLabel = "Deshacer"
            )
            if (result == SnackbarResult.ActionPerformed) {
                onRestoreRoutine(routine)
            }
            recentlyDeletedRoutine = null
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onStartClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar rutina")
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                item {
                    Text(
                        text = "Mi Rutina Interactiva",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Organiza y completa tus rutinas diarias de forma divertida",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Rutinas activas
                item {
                    Text(
                        text = "Rutinas activas",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (routines.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "No hay rutinas activas",
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                } else {
                    items(routines) { routine ->
                        RoutineCard(
                            routine = routine,
                            onClick = { onRoutineClick(routine) },
                            onDelete = {
                                onDeleteRoutine(routine)
                                recentlyDeletedRoutine = routine // guarda para deshacer
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // Rutinas completadas
                if (completedRoutines.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Rutinas completadas",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(completedRoutines) { routine ->
                        RoutineCard(
                            routine = routine,
                            onClick = { /* opcional */ },
                            onDelete = {
                                onDeleteRoutine(routine)
                                recentlyDeletedRoutine = routine // guarda para deshacer
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}