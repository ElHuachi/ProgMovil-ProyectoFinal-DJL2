package com.daviddj.proyecto_final_djl.data

import android.content.Context

interface AppContainer {
    val notasRepository: NotasRepository
    val tareasRepository: TareasRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val notasRepository: NotasRepository by lazy {
        OfflineNotasRepository(NotasDatabase.getDatabase(context).notaDao())
    }

    override val tareasRepository: TareasRepository by lazy {
        OfflineTareasRepository(TareasDatabase.getDatabase(context).tareaDao())
    }
}