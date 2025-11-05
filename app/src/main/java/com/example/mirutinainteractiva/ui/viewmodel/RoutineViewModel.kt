package com.example.mirutinainteractiva.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mirutinainteractiva.data.local.RoutineEntity
import com.example.mirutinainteractiva.repository.RoutineRepository
import com.example.mirutinainteractiva.data.local.SubtaskEntity
import com.example.mirutinainteractiva.data.local.RoutineWithSubtasks
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.Flow
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

    // Subtasks
    fun addSubtask(routineId: Int, title: String) {
        viewModelScope.launch {
            repository.insertSubtask(SubtaskEntity(routineId = routineId, title = title))
        }
    }

    fun toggleSubtask(subtask: SubtaskEntity) {
        viewModelScope.launch {
            repository.updateSubtask(subtask.copy(completed = !subtask.completed))
        }
    }

    fun getRoutineWithSubtasks(routineId: Int): Flow<RoutineWithSubtasks> {
        return repository.getRoutineWithSubtasks(routineId)
    }

    fun addRoutineWithSubtasks(
        title: String,
        description: String,
        difficulty: String,
        imageRes: Int? = null,
        completed: Boolean = false,
        subtasks: List<String>
    ) {
        viewModelScope.launch {
            val routineId = repository.insertRoutine(
                RoutineEntity(
                    title = title,
                    description = description,
                    difficulty = difficulty,
                    imageRes = imageRes,
                    completed = completed
                )
            ).toInt()

            subtasks.forEach { subtaskTitle ->
                repository.insertSubtask(
                    SubtaskEntity(
                        routineId = routineId,
                        title = subtaskTitle
                    )
                )
            }
        }
    }
}