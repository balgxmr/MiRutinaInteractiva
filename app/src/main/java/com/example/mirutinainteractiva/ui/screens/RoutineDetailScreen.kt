package com.example.mirutinainteractiva.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mirutinainteractiva.data.local.RoutineEntity
import com.example.mirutinainteractiva.navigation.Screen
import com.example.mirutinainteractiva.ui.viewmodel.RoutineViewModel
import kotlinx.coroutines.launch

@Composable
fun RoutineDetailScreen(
    routine: PredefinedRoutine,
    routineViewModel: RoutineViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val difficulty = when {
        routine.subtasks.size <= 3 -> "Fácil"
        routine.subtasks.size <= 5 -> "Intermedio"
        else -> "Difícil"
    }

    Column(modifier = Modifier.padding(24.dp)) {
        Text(routine.title, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(routine.description, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Dificultad: $difficulty", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(16.dp))
        Text("Subtareas:", style = MaterialTheme.typography.titleMedium)
        routine.subtasks.forEach { subtask ->
            Text("• $subtask", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                coroutineScope.launch {
                    routineViewModel.addRoutineWithSubtasks(
                        title = routine.title,
                        description = routine.description,
                        difficulty = difficulty,
                        subtasks = routine.subtasks,
                        timeOfDay = "Mañana"
                    )
                    Toast.makeText(context, "Rutina empezada", Toast.LENGTH_SHORT).show()
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        ) {
            Text("Comenzar rutina")
        }
    }
}