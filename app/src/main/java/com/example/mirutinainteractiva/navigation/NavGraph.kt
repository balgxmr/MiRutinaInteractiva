package com.example.mirutinainteractiva.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mirutinainteractiva.data.models.Routine
import com.example.mirutinainteractiva.ui.components.BottomBar
import com.example.mirutinainteractiva.ui.screens.MainScreen
import com.example.mirutinainteractiva.ui.screens.RoutineExecutionScreen
import com.example.mirutinainteractiva.ui.screens.RoutineSelectionScreen
import com.example.mirutinainteractiva.ui.viewmodel.RoutineViewModel

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Welcome : Screen("welcome", "Inicio", Icons.Default.Home)
    object RoutineSelection : Screen("routineSelection", "Rutinas", Icons.Default.List)
    object Summary : Screen("summary", "Progreso", Icons.Default.Favorite)
    object RoutineExecution : Screen("routineExecution", "Ejecutar", Icons.Default.PlayArrow)
}


@Composable
fun AppNavGraph(navController: NavHostController, routineViewModel: RoutineViewModel) {
    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Welcome.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.RoutineSelection.route) {
                RoutineSelectionScreen(
                    routineViewModel = routineViewModel,
                    onRoutineCreated = {
                        // Cuando se guarda una rutina, volvemos a la pantalla principal
                        navController.navigate(Screen.Welcome.route) {
                            popUpTo(Screen.Welcome.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.RoutineExecution.route) {
                // Aquí deberías recibir la rutina seleccionada como argumento
                val dummyRoutine = Routine(
                    id = 1,
                    title = "Rutina de ejemplo",
                    description = "Ejemplo de ejecución paso a paso",
                    difficulty = "Fácil",
                    imageRes = null
                )

                RoutineExecutionScreen(
                    routine = dummyRoutine,
                    onFinish = { navController.navigate(Screen.Summary.route) }
                )
            }

            composable(Screen.Summary.route) {
                // Aquí luego implementamos la pantalla de progreso/resumen
                Text("Pantalla de progreso")
            }

            composable(Screen.Welcome.route) {
                val routinesEntities = routineViewModel.routines.collectAsState(initial = emptyList()).value

                MainScreen(
                    routines = routinesEntities.map {
                        com.example.mirutinainteractiva.data.models.Routine(
                            id = it.id,
                            title = it.title,
                            description = it.description,
                            difficulty = it.difficulty,
                            imageRes = it.imageRes
                        )
                    },
                    onStartClick = { navController.navigate(Screen.RoutineSelection.route) },
                    onRoutineClick = { routine ->
                        navController.navigate(Screen.RoutineExecution.route)
                    }
                )
            }
            // demás pantallas...
        }
    }
}