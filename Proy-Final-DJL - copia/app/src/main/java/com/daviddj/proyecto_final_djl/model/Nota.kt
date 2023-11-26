package com.daviddj.proyecto_final_djl.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.Date
@Entity(tableName = "notas")
data class Nota(
    @PrimaryKey (autoGenerate = true) val id: Int=0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "fecha") val fecha: String,
    @ColumnInfo(name = "contenido") var contenido: String,
    @ColumnInfo(name = "imageUris") var imageUris: String,
    @ColumnInfo(name = "videoUris") var videoUris: String
)
