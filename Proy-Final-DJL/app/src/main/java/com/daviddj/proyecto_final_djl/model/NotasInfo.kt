package com.daviddj.proyecto_final_djl.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.util.Date

object NotasInfo{
    @RequiresApi(Build.VERSION_CODES.O)
    val notas = listOf(
        Nota(
            id = 1,
            name = "Nota de ejemplo 1",
            description = "Descripción...",
            fecha = "03/11/2023",
            contenido = "Contenido de ejemplo para pruebas de funcionalidad"
        ),
        Nota(
            id = 2,
            name = "Nota de ejemplo 2",
            description = "Descripción",
            fecha = "03/11/2023",
            contenido = ""
        ),
    )
}