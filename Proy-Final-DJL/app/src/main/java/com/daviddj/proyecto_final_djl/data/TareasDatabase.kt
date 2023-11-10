package com.daviddj.proyecto_final_djl.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.daviddj.proyecto_final_djl.model.Tarea

@Database(entities = [Tarea::class], version = 3, exportSchema = false)
abstract class TareasDatabase : RoomDatabase() {
    abstract fun tareaDao(): TareaDAO

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