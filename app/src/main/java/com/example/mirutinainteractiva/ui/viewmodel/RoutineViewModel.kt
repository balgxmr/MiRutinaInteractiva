package com.example.mirutinainteractiva.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mirutinainteractiva.data.local.RoutineEntity
import com.example.mirutinainteractiva.repository.RoutineRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RoutineViewModel(private val repository: RoutineRepository) : ViewModel() {

    val routines: StateFlow<List<RoutineEntity>> =
        repository.getAllRoutines()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addRoutine(title: String, description: String, difficulty: String, imageRes: Int? = null, completed: Boolean = false) {
        viewModelScope.launch {
            repository.insertRoutine(
                RoutineEntity(
                    title = title,
                    description = description,
                    difficulty = difficulty,
                    imageRes = imageRes,
                    completed = completed
                )
            )
        }
    }

    fun deleteRoutine(routine: RoutineEntity) {
        viewModelScope.launch {
            repository.deleteRoutine(routine)
        }
    }

    fun markRoutineAsCompleted(id: Int) {
        viewModelScope.launch {
            repository.markRoutineAsCompleted(id)
        }
    }
}