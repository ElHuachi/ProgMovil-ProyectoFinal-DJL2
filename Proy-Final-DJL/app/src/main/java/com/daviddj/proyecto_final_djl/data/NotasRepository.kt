package com.daviddj.proyecto_final_djl.data

import com.daviddj.proyecto_final_djl.model.Nota
import kotlinx.coroutines.flow.Flow

interface NotasRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllItemsStream(): Flow<List<Nota>>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getItemStream(id: Int): Flow<Nota?>

    /**
     * Insert item in the data source
     */
    suspend fun insertItem(nota: Nota)

    /**
     * Delete item from the data source
     */
    suspend fun deleteItem(nota: Nota)

    /**
     * Update item in the data source
     */
    suspend fun updateItem(nota: Nota)

}