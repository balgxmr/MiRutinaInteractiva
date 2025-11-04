package com.example.mirutinainteractiva.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mirutinainteractiva.data.models.Routine

@Composable
fun RoutineExecutionScreen(
    routine: Routine, // la rutina seleccionada
    onFinish: () -> Unit
) {
    // Simulación de pasos de la rutina
    val steps = listOf(
        "Preparar materiales",
        "Realizar actividad principal",
        "Guardar y limpiar"
    )

    var currentStep by remember { mutableStateOf(0) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Encabezado
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = routine.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Dificultad: ${routine.difficulty}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Imagen opcional
            routine.imageRes?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = routine.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
            }

            // Paso actual
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Paso ${currentStep + 1} de ${steps.size}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = steps[currentStep],
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }

            // Barra de progreso
            LinearProgressIndicator(
                progress = (currentStep + 1) / steps.size.toFloat(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )

            // Botón siguiente/finalizar
            Button(
                onClick = {
                    if (currentStep < steps.lastIndex) {
                        currentStep++
                    } else {
                        onFinish()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (currentStep < steps.lastIndex) "Siguiente" else "Finalizar")
            }
        }
    }
}