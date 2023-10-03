package com.daviddj.proyecto_final_djl.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.util.Date

object TareasInfo {
    @RequiresApi(Build.VERSION_CODES.O)
    val tareas = listOf<Tarea>(
        Tarea(
            name = "Tarea de ejemplo 1",
            description = "Descripción...",
            fecha = Date.from(Instant.now()),
            isComplete = false,
            contenido = ""
        ),
        Tarea(
            name = "Tarea de ejemplo 2",
            description = "Descripción...",
            fecha = Date.from(Instant.now()),
            isComplete = false,
            contenido = ""
        ),
    )
}