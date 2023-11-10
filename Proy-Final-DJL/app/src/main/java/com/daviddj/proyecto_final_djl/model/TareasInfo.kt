package com.daviddj.proyecto_final_djl.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.util.Date

object TareasInfo {
    @RequiresApi(Build.VERSION_CODES.O)
    val tareas = listOf<Tarea>(
        Tarea(
            id = 1,
            name = "Tarea de ejemplo 1",
            description = "Descripción...",
            fecha = "03/11/2023",
            isComplete = false,
            contenido = "Contenido de ejemplo para pruebas de funcionalidad"
        ),
        Tarea(
            id = 2,
            name = "Tarea de ejemplo 2",
            description = "Descripción...",
            fecha = "03/11/2023",
            isComplete = false,
            contenido = ""
        ),
    )
}