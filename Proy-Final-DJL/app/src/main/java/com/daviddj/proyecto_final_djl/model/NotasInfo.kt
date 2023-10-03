package com.daviddj.proyecto_final_djl.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.util.Date

object NotasInfo{
    @RequiresApi(Build.VERSION_CODES.O)
    val notas = listOf(
        Nota(
            name = "Nota de ejemplo 1",
            description = "Descripción...",
            fecha = Date.from(Instant.now()),
            contenido = ""
        ),
        Nota(
            name = "Nota de ejemplo 2",
            description = "Descripción",
            fecha = Date.from(Instant.now()),
            contenido = ""
        ),
    )
}