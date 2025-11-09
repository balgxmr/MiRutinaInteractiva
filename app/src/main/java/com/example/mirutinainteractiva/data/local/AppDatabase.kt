package com.example.mirutinainteractiva.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RoutineEntity::class, SubtaskEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao
}