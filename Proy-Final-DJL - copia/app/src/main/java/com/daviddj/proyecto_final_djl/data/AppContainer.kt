package com.daviddj.proyecto_final_djl.data

import android.content.Context

interface AppContainer {
    val notasRepository: NotasRepository
    val notasMultimediaRepository: NotaMultimediaRepository
    val tareasRepository: TareasRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val notasRepository: NotasRepository by lazy {
        OfflineNotasRepository(NotasDatabase.getDatabase(context).notaDao())
    }

    override val notasMultimediaRepository: NotaMultimediaRepository by lazy {
        OfflineNotaMultimediaRepository(NotasDatabase.getDatabase(context).notaMultimediaDao())
    }

    override val tareasRepository: TareasRepository by lazy {
        OfflineTareasRepository(TareasDatabase.getDatabase(context).tareaDao())
    }
}