package com.example.mirutinainteractiva.repository

import com.example.mirutinainteractiva.data.local.RoutineDao
import com.example.mirutinainteractiva.data.local.RoutineEntity
import kotlinx.coroutines.flow.Flow

class RoutineRepository(private val dao: RoutineDao) {

    fun getAllRoutines(): Flow<List<RoutineEntity>> = dao.getAllRoutines()

    suspend fun insertRoutine(routine: RoutineEntity) {
        dao.insertRoutine(routine)
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

}