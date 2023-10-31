package com.daviddj.proyecto_final_djl.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.Date
data class Tarea (
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "fecha") val fecha: LocalDateTime,
    @ColumnInfo(name = "isComplete") var isComplete: Boolean,
    @ColumnInfo(name = "contenido") var contenido: String
)
