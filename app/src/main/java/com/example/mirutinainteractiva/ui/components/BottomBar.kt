package com.example.mirutinainteractiva.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mirutinainteractiva.navigation.Screen
import androidx.compose.runtime.getValue

@Composable
fun BottomBar(navController: NavController) {
    val items = listOf(
        Screen.Welcome,
        Screen.RoutinePicker,
        Screen.Summary
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        // Limpiamos el back stack hasta la raíz
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = false
                            saveState = false // No guardar el estado anterior
                        }
                        launchSingleTop = true
                        restoreState = false // No restaurar pantallas previas
                    }
                },
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) }
            )
        }
    }
}