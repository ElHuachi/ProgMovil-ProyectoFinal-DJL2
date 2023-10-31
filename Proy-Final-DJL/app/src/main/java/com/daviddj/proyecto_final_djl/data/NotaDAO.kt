package com.daviddj.proyecto_final_djl.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.daviddj.proyecto_final_djl.model.Nota

@Dao
interface NotaDAO {
    /*
    @Query("SELECT * FROM notas")
    fun getAll(): List<Nota>
    */
    @Insert
    fun insertAll(nota: Nota)

    @Delete
    fun delete(nota: Nota)

    @Update
    fun update(nota: Nota)
}
