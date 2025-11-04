package com.example.mirutinainteractiva.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Routine(
    val id: Int,
    val title: String,
    val description: String,
    val difficulty: String,
    val imageRes: Int? = null,
    val completed: Boolean = false
) : Parcelable