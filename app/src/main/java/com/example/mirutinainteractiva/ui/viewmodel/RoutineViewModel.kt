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

    init {
        preloadDefaultRoutines()
    }

    fun addRoutine(
        title: String,
        description: String,
        difficulty: String,
        imageRes: Int? = null,
        completed: Boolean = false,
        timeOfDay: String = "Mañana"
    ) {
        viewModelScope.launch {
            repository.insertRoutine(
                RoutineEntity(
                    title = title,
                    description = description,
                    difficulty = difficulty,
                    imageRes = imageRes,
                    completed = completed,
                    isPredefined = false,
                    timeOfDay = timeOfDay
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
        subtasks: List<String>,
        timeOfDay: String = "Mañana"
    ) {
        viewModelScope.launch {
            val routineId = repository.insertRoutine(
                RoutineEntity(
                    title = title,
                    description = description,
                    difficulty = difficulty,
                    imageRes = imageRes,
                    completed = completed,
                    isPredefined = false,
                    timeOfDay = timeOfDay
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

    private fun calculateDifficulty(subtaskCount: Int): String {
        return when {
            subtaskCount >= 7 -> "Difícil"
            subtaskCount >= 5 -> "Intermedio"
            else -> "Fácil"
        }
    }

    // Cargar rutinas preestablecidas
    fun preloadDefaultRoutines() {
        viewModelScope.launch {
            val existing = repository.getAllRoutinesOnce()
            if (existing.isEmpty()) {
                val predefinedRoutines = listOf(
                    RoutineEntity(
                        title = "Hacer la cama",
                        description = "Ordenar las sábanas y colocar las almohadas.",
                        difficulty = calculateDifficulty(3),
                        imageRes = null,
                        completed = false,
                        isPredefined = true,
                        timeOfDay = "Mañana"
                    ),
                    RoutineEntity(
                        title = "Bañarse",
                        description = "Lavarse bien para empezar el día.",
                        difficulty = calculateDifficulty(5),
                        imageRes = null,
                        completed = false,
                        isPredefined = true,
                        timeOfDay = "Mañana"
                    ),
                    RoutineEntity(
                        title = "Hacer la tarea",
                        description = "Completa tus deberes escolares.",
                        difficulty = calculateDifficulty(7),
                        imageRes = null,
                        completed = false,
                        isPredefined = true,
                        timeOfDay = "Tarde"
                    )
                )

                predefinedRoutines.forEach { routine ->
                    val routineId = repository.insertRoutine(routine).toInt()
                    val subtasks = when (routine.title) {
                        "Hacer la cama" -> listOf("Sacar las sábanas", "Doblar la cobija", "Poner la almohada")
                        "Bañarse" -> listOf("Abrir la ducha", "Usar jabón", "Enjuagarse", "Secarse", "Vestirse")
                        "Hacer la tarea" -> listOf(
                            "Preparar el cuaderno",
                            "Leer instrucciones",
                            "Resolver ejercicios",
                            "Revisar errores",
                            "Guardar materiales",
                            "Organizar mochila",
                            "Lavar manos"
                        )
                        else -> emptyList()
                    }
                    subtasks.forEach { title ->
                        repository.insertSubtask(SubtaskEntity(routineId = routineId, title = title))
                    }
                }
            }
        }
    }
}