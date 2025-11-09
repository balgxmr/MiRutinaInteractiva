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
import com.example.mirutinainteractiva.data.local.RoutineEntity
import com.example.mirutinainteractiva.data.models.Routine
import com.example.mirutinainteractiva.ui.components.BottomBar
import com.example.mirutinainteractiva.ui.screens.MainScreen
import com.example.mirutinainteractiva.ui.screens.RoutineExecutionScreen
import com.example.mirutinainteractiva.ui.screens.CreateRoutineScreen
import com.example.mirutinainteractiva.ui.screens.RoutinePickerScreen
import com.example.mirutinainteractiva.ui.screens.SummaryScreen
import com.example.mirutinainteractiva.ui.viewmodel.RoutineViewModel
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import com.example.mirutinainteractiva.ui.screens.PredefinedRoutine
import com.example.mirutinainteractiva.ui.screens.RoutineDetailScreen

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Welcome : Screen("welcome", "Inicio", Icons.Default.Home)
    object RoutinePicker : Screen("routinePicker", "Rutinas", Icons.Default.List)
    object RoutineCreate : Screen("routineCreate", "Crear rutina", Icons.Default.List)
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
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                fadeIn(animationSpec = tween(500))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(500))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(500))
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(500))
            }
        ) {
            // Pantalla para elegir rutina preestablecida
            composable(Screen.RoutinePicker.route) {
                RoutinePickerScreen(
                    navController = navController,
                    routineViewModel = routineViewModel,
                    onRoutineSelected = { routine ->
                        navController.navigate("${Screen.RoutineExecution.route}/${routine.id}")
                    },
                    onCreateRoutineClick = {
                        navController.navigate(Screen.RoutineCreate.route)
                    }
                )
            }

            // Pantalla para crear rutina personalizada
            composable(Screen.RoutineCreate.route) {
                CreateRoutineScreen(
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
                val id = backStackEntry.arguments?.getString("routineId")?.toIntOrNull()
                val routine = routineViewModel.routines.collectAsState(initial = emptyList()).value
                    .map {
                        Routine(
                            id = it.id,
                            title = it.title,
                            description = it.description,
                            difficulty = it.difficulty,
                            imageRes = it.imageRes,
                            completed = it.completed
                        )
                    }
                    .find { it.id == id }

                routine?.let {
                    RoutineExecutionScreen(
                        routine = it,
                        routineViewModel = routineViewModel,
                        onFinish = {
                            routineViewModel.markRoutineAsCompleted(it.id)
                            navController.currentBackStackEntry?.savedStateHandle?.set("completedRoutine", it)
                            navController.navigate(Screen.Summary.route) {
                                popUpTo(Screen.Welcome.route) { saveState = true }
                                launchSingleTop = true
                            }
                        },
                        onDelete = { routineToDelete ->
                            routineViewModel.deleteRoutine(
                                RoutineEntity(
                                    id = routineToDelete.id,
                                    title = routineToDelete.title,
                                    description = routineToDelete.description,
                                    difficulty = routineToDelete.difficulty,
                                    imageRes = routineToDelete.imageRes,
                                    completed = routineToDelete.completed,
                                    isPredefined = false,
                                    timeOfDay = "Mañana" // valor por defecto
                                )
                            )
                            navController.navigate(Screen.Welcome.route) {
                                popUpTo(Screen.Welcome.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                } ?: Text("No se encontró la rutina seleccionada")
            }

            // Pantalla de progreso/resumen
            composable(Screen.Summary.route) {
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
                val completedRoutines = routinesEntities.filter { it.completed }

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
                    completedRoutines = completedRoutines.map {
                        Routine(
                            id = it.id,
                            title = it.title,
                            description = it.description,
                            difficulty = it.difficulty,
                            imageRes = it.imageRes,
                            completed = it.completed
                        )
                    },
                    onStartClick = { navController.navigate(Screen.RoutinePicker.route) }, // 👈 ahora va al picker
                    onRoutineClick = { routine ->
                        navController.navigate("${Screen.RoutineExecution.route}/${routine.id}")
                    },
                    onDeleteRoutine = { routineToDelete ->
                        routineViewModel.deleteRoutine(
                            RoutineEntity(
                                id = routineToDelete.id,
                                title = routineToDelete.title,
                                description = routineToDelete.description,
                                difficulty = routineToDelete.difficulty,
                                imageRes = routineToDelete.imageRes,
                                completed = routineToDelete.completed,
                                isPredefined = false,
                                timeOfDay = "Mañana" // valor por defecto
                            )
                        )
                    },
                    onRestoreRoutine = { routineToRestore ->
                        routineViewModel.addRoutine(
                            title = routineToRestore.title,
                            description = routineToRestore.description,
                            difficulty = routineToRestore.difficulty,
                            imageRes = routineToRestore.imageRes,
                            completed = routineToRestore.completed
                        )
                    }
                )
            }

            // Pantalla para elegir rutina preestablecida
            composable(Screen.RoutinePicker.route) {
                RoutinePickerScreen(
                    navController = navController, // 👈 ahora se pasa
                    routineViewModel = routineViewModel,
                    onRoutineSelected = { routine ->
                        navController.navigate("${Screen.RoutineExecution.route}/${routine.id}")
                    },
                    onCreateRoutineClick = {
                        navController.navigate(Screen.RoutineCreate.route)
                    }
                )
            }

            // Pantalla de detalle de rutina preestablecida
            composable("routineDetail") {
                val routine = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<PredefinedRoutine>("selectedRoutine")

                routine?.let {
                    RoutineDetailScreen(
                        routine = it,
                        routineViewModel = routineViewModel,
                        onStartRoutine = { entity ->
                            navController.navigate("${Screen.RoutineExecution.route}/${entity.id}")
                        }
                    )
                } ?: Text("No se encontró la rutina seleccionada")
            }
        }
    }
}