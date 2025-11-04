package com.example.mirutinainteractiva

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.compose.AppTheme
import com.example.mirutinainteractiva.data.local.AppDatabase
import com.example.mirutinainteractiva.repository.RoutineRepository
import com.example.mirutinainteractiva.navigation.AppNavGraph
import com.example.mirutinainteractiva.ui.viewmodel.RoutineViewModel
import com.example.mirutinainteractiva.ui.viewmodel.RoutineViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "routines_db"
        ).build()

        val repository = RoutineRepository(db.routineDao())
        val factory = RoutineViewModelFactory(repository)

        setContent {
            AppTheme {
                val navController = rememberNavController()
                val routineViewModel: RoutineViewModel = viewModel(factory = factory)

                AppNavGraph(navController = navController, routineViewModel = routineViewModel)
            }
        }
    }
}