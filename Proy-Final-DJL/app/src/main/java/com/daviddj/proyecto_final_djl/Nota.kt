package com.daviddj.proyecto_final_djl

import java.util.Date
data class Nota(
    var name: String,
    var description: String,
    val fecha: Date,
    var contenido: String
)