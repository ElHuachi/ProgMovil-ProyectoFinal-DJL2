package com.daviddj.proyecto_final_djl.data

import com.daviddj.proyecto_final_djl.model.NotaMultimedia
import com.daviddj.proyecto_final_djl.model.TareaMultimedia
import kotlinx.coroutines.flow.Flow

interface TareaMultimediaRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllItemsStream(): Flow<List<TareaMultimedia>>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getItemStream(id: Int): Flow<TareaMultimedia?>

    /**
     * Retrieve all the items from the given data source that matches with the [notaId].
     */
    fun getItemsStreamById(tareaId: Int): Flow<List<TareaMultimedia>>

    /**
     * Insert item in the data source
     */
    suspend fun insertItem(tareaMultimedia: TareaMultimedia)

    /**
     * Delete item from the data source
     */
    suspend fun deleteItem(tareaMultimedia: TareaMultimedia)

    /**
     * Update item in the data source
     */
    suspend fun updateItem(tareaMultimedia: TareaMultimedia)


    //suspend fun getAllById(idNota: Int)
}