package com.example.mirutinainteractiva

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.compose.AppTheme
import com.example.mirutinainteractiva.data.local.AppDatabase
import com.example.mirutinainteractiva.repository.RoutineRepository
import com.example.mirutinainteractiva.navigation.AppNavGraph
import com.example.mirutinainteractiva.ui.viewmodel.RoutineViewModel
import com.example.mirutinainteractiva.ui.viewmodel.RoutineViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE routines ADD COLUMN isPredefined INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE routines ADD COLUMN timeOfDay TEXT NOT NULL DEFAULT 'Mañana'")
            }
        }

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "routines_db"
        )
            .addMigrations(MIGRATION_2_3) // aplica migración
            .build()

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