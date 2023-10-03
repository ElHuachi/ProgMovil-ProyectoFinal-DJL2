package com.daviddj.proyecto_final_djl.model

import java.util.Date
data class Tarea (
    var name: String,
    var description: String,
    val fecha: Date,
    var isComplete: Boolean,
    var contenido: String
)