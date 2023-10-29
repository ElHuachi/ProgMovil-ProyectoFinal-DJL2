package com.daviddj.proyecto_final_djl.model

import java.time.LocalDateTime
import java.util.Date
data class Nota(
    var id: Int,
    var name: String,
    var description: String,
    val fecha: LocalDateTime,
    var contenido: String
)