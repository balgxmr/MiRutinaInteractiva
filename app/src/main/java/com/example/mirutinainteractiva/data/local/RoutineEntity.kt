package com.example.mirutinainteractiva.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routines")
data class RoutineEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val difficulty: String,
    val imageRes: Int? = null
)