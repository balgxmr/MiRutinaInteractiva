package com.example.mirutinainteractiva.repository

import com.example.mirutinainteractiva.data.local.RoutineDao
import com.example.mirutinainteractiva.data.local.RoutineEntity
import com.example.mirutinainteractiva.data.local.SubtaskEntity
import com.example.mirutinainteractiva.data.local.RoutineWithSubtasks
import kotlinx.coroutines.flow.Flow

class RoutineRepository(private val dao: RoutineDao) {

    fun getAllRoutines(): Flow<List<RoutineEntity>> = dao.getAllRoutines()

    suspend fun getAllRoutinesOnce(): List<RoutineEntity> = dao.getAllRoutinesOnce()

    suspend fun insertRoutine(routine: RoutineEntity): Long {
        return dao.insertRoutine(routine)
    }

    suspend fun deleteRoutine(routine: RoutineEntity) {
        dao.deleteRoutine(routine)
    }

    suspend fun updateRoutine(routine: RoutineEntity) {
        dao.updateRoutine(routine)
    }

    suspend fun markRoutineAsCompleted(id: Int) {
        val routine = dao.getRoutineById(id)
        dao.updateRoutine(routine.copy(completed = true))
    }

    // Subtasks
    suspend fun insertSubtask(subtask: SubtaskEntity) = dao.insertSubtask(subtask)

    suspend fun updateSubtask(subtask: SubtaskEntity) = dao.updateSubtask(subtask)

    fun getRoutineWithSubtasks(routineId: Int): Flow<RoutineWithSubtasks> =
        dao.getRoutineWithSubtasks(routineId)
}