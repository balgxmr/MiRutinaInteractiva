package com.example.mirutinainteractiva

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mirutinainteractiva.ui.theme.AppTheme
import com.example.mirutinainteractiva.ui.screens.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                MainScreen(onStartClick = {
                    // Aquí luego navegas a la pantalla de selección de rutina
                })
            }
        }
    }
}