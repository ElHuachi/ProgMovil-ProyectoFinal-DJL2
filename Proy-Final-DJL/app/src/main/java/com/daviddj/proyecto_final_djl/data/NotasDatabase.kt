package com.daviddj.proyecto_final_djl.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.daviddj.proyecto_final_djl.model.Nota

@Database(entities = [Nota::class], version = 3, exportSchema = false)
abstract class NotasDatabase : RoomDatabase() {
    abstract fun notaDao(): NotaDAO

    companion object {
        @Volatile
        private var Instance: NotasDatabase? = null

        fun getDatabase(context: Context): NotasDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, NotasDatabase::class.java, "notas_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }


}