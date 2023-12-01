package com.daviddj.proyecto_final_djl.data

import com.daviddj.proyecto_final_djl.model.TareaMultimedia
import kotlinx.coroutines.flow.Flow

class OfflineTareaMultimediaRepository(private val tareaMultimediaDAO: TareaMultimediaDAO) : TareaMultimediaRepository {
    override fun getAllItemsStream(): Flow<List<TareaMultimedia>> = tareaMultimediaDAO.getAllItems()

    override fun getItemStream(id: Int): Flow<TareaMultimedia?> = tareaMultimediaDAO.getItem(id)

    override fun getItemsStreamById(tareaId: Int): Flow<List<TareaMultimedia>> = tareaMultimediaDAO.getAllById(tareaId)

    override suspend fun insertItem(tareaMultimedia: TareaMultimedia) = tareaMultimediaDAO.insert(tareaMultimedia)

    override suspend fun deleteItem(tareaMultimedia: TareaMultimedia) = tareaMultimediaDAO.delete(tareaMultimedia)

    override suspend fun updateItem(tareaMultimedia: TareaMultimedia) = tareaMultimediaDAO.update(tareaMultimedia)
}