package com.example.mirutinainteractiva.data.local

import androidx.room.Embedded
import androidx.room.Relation

data class RoutineWithSubtasks(
    @Embedded val routine: RoutineEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "routineId"
    )
    val subtasks: List<SubtaskEntity>
)