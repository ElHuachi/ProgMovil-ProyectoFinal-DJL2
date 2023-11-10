package com.daviddj.proyecto_final_djl.data

import com.daviddj.proyecto_final_djl.model.Nota
import kotlinx.coroutines.flow.Flow

class NotasRepositoryImpl : NotasRepository {
    override fun getAllItemsStream(): Flow<List<Nota>> {
        // Implementa este método para devolver un flujo de todas las notas
        return TODO("Provide the return value")
    }

    override fun getItemStream(id: Int): Flow<Nota?> {
        // Implementa este método para devolver un flujo de una nota específica basada en su id
        return TODO("Provide the return value")
    }

    override suspend fun insertItem(nota: Nota) {
        // Implementa este método para insertar una nota
    }

    override suspend fun deleteItem(nota: Nota) {
        // Implementa este método para eliminar una nota
    }

    override suspend fun updateItem(nota: Nota) {
        // Implementa este método para actualizar una nota
    }
}