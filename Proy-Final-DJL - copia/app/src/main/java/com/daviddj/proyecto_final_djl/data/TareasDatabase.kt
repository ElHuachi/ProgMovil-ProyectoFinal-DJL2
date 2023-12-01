package com.daviddj.proyecto_final_djl.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.daviddj.proyecto_final_djl.model.NotaMultimedia
import com.daviddj.proyecto_final_djl.model.Tarea
import com.daviddj.proyecto_final_djl.model.TareaMultimedia

@Database(entities = [Tarea::class, TareaMultimedia::class], version = 9, exportSchema = false)
abstract class TareasDatabase : RoomDatabase() {
    abstract fun tareaDao(): TareaDAO

    abstract fun tareaMultimediaDao(): TareaMultimediaDAO

    companion object {
        @Volatile
        private var Instance: TareasDatabase? = null

        fun getDatabase(context: Context): TareasDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, TareasDatabase::class.java, "tarea_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }

}