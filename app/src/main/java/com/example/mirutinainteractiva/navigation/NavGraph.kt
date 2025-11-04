package com.example.mirutinainteractiva.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mirutinainteractiva.ui.components.BottomBar
import com.example.mirutinainteractiva.ui.screens.MainScreen
import com.example.mirutinainteractiva.ui.screens.RoutineSelectionScreen

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Welcome : Screen("welcome", "Inicio", Icons.Default.Home)
    object RoutineSelection : Screen("routineSelection", "Rutinas", Icons.Default.List)
    object Summary : Screen("summary", "Progreso", Icons.Default.Favorite)
}


@Composable
fun AppNavGraph(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Welcome.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Welcome.route) {
                MainScreen(onStartClick = {
                    navController.navigate(Screen.RoutineSelection.route)
                })
            }
            composable(Screen.RoutineSelection.route) {
                RoutineSelectionScreen(
                    onRoutineSelected = { navController.navigate(Screen.Summary.route) }
                )
            }
            composable(Screen.Summary.route) {
                // Pantalla de progreso/resumen
            }
        }
    }
}