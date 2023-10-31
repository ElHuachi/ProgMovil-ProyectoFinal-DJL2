package com.daviddj.proyecto_final_djl.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.daviddj.proyecto_final_djl.model.Tarea

@Dao
interface TareaDAO {
    /*
    @Query("SELECT * FROM tareas")
    fun getAll(): List<Tareas>
    */
    @Insert
    fun insertAll(tarea: Tarea)

    @Delete
    fun delete(tarea: Tarea)

    @Update
    fun update(tarea: Tarea)
}
