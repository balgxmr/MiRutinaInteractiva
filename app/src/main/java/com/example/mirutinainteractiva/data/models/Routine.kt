package com.example.mirutinainteractiva.data.models

data class Routine(
    val id: Int,
    val title: String,
    val description: String,
    val difficulty: String,
    val imageRes: Int? = null // opcional, recurso drawable
)