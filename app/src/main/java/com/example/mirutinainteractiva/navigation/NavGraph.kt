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
import com.example.mirutinainteractiva.ui.screens.SummaryScreen
import com.example.mirutinainteractiva.ui.viewmodel.RoutineViewModel

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Welcome : Screen("welcome", "Inicio", Icons.Default.Home)
    object RoutineSelection : Screen("routineSelection", "Rutinas", Icons.Default.List)
    object Summary : Screen("summary", "Progreso", Icons.Default.Favorite)
    object RoutineExecution : Screen("routineExecution", "Ejecutar", Icons.Default.PlayArrow) {
        const val ROUTE_WITH_ARG = "routineExecution/{routineId}"
    }
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
            // Pantalla de selección/creación de rutinas
            composable(Screen.RoutineSelection.route) {
                RoutineSelectionScreen(
                    routineViewModel = routineViewModel,
                    onRoutineCreated = {
                        navController.navigate(Screen.Welcome.route) {
                            popUpTo(Screen.Welcome.route) { inclusive = true }
                        }
                    }
                )
            }

            // Pantalla de ejecución de rutina con argumento ID
            composable(Screen.RoutineExecution.ROUTE_WITH_ARG) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("routineId")?.toInt()
                val routine = routineViewModel.routines.collectAsState(initial = emptyList()).value
                    .map {
                        Routine(
                            id = it.id,
                            title = it.title,
                            description = it.description,
                            difficulty = it.difficulty,
                            imageRes = it.imageRes
                        )
                    }
                    .find { it.id == id }

                routine?.let {
                    RoutineExecutionScreen(
                        routine = it,
                        onFinish = {
                            routineViewModel.markRoutineAsCompleted(it.id)
                            navController.currentBackStackEntry?.savedStateHandle?.set("completedRoutine", it)
                            navController.navigate(Screen.Summary.route) {
                                popUpTo(Screen.Welcome.route) { saveState = true }
                                launchSingleTop = true
                            }
                        }
                    )
                } ?: Text("No se encontró la rutina seleccionada")
            }

            // Pantalla de progreso/resumen
            composable(Screen.Summary.route) { backStackEntry ->
                val routine = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<Routine>("completedRoutine")

                SummaryScreen(
                    routine = routine,
                    onBackToHome = {
                        navController.navigate(Screen.Welcome.route) {
                            popUpTo(Screen.Welcome.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            // Pantalla principal (Welcome)
            composable(Screen.Welcome.route) {
                val routinesEntities = routineViewModel.routines.collectAsState(initial = emptyList()).value

                val activeRoutines = routinesEntities.filter { !it.completed }
                val completedCount = routinesEntities.count { it.completed }

                MainScreen(
                    routines = activeRoutines.map {
                        Routine(
                            id = it.id,
                            title = it.title,
                            description = it.description,
                            difficulty = it.difficulty,
                            imageRes = it.imageRes,
                            completed = it.completed
                        )
                    },
                    completedCount = completedCount,
                    onStartClick = { navController.navigate(Screen.RoutineSelection.route) },
                    onRoutineClick = { routine ->
                        navController.navigate("${Screen.RoutineExecution.route}/${routine.id}")
                    }
                )
            }
        }
    }
}