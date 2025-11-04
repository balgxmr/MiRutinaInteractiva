package com.example.mirutinainteractiva.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mirutinainteractiva.data.models.Routine
import com.example.mirutinainteractiva.ui.components.RoutineCard

@Composable
fun MainScreen(
    routines: List<Routine>,
    onStartClick: () -> Unit,
    onRoutineClick: (Routine) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (routines.isEmpty()) {
            // Estado vacío
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Aún no has creado rutinas",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Presiona el botón para comenzar a crear tu primera rutina",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(onClick = onStartClick) {
                    Text("Comenzar")
                }
            }
        } else {
            // Mostrar rutinas en Cards
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(routines) { routine ->
                    RoutineCard(routine = routine) {
                        onRoutineClick(routine)
                    }
                }
            }
        }
    }
}