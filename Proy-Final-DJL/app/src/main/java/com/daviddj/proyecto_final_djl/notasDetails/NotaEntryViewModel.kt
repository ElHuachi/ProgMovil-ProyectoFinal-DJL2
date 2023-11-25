package com.daviddj.proyecto_final_djl.notaDetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.daviddj.proyecto_final_djl.data.NotasRepository
import com.daviddj.proyecto_final_djl.model.Nota


class ItemEntryViewModel(private val itemsRepository: NotasRepository) : ViewModel() {

    var itemUiState by mutableStateOf(ItemUiState())
        private set


    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }

    suspend fun saveItem() {
        if (validateInput()) {
            itemsRepository.insertItem(itemUiState.itemDetails.toItem())
        }
    }

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && description.isNotBlank()
        }
    }
}

data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false
)

data class ItemDetails(
    val id: Int = 0,
    val name: String = "",
    val description:String = "",
    val fecha: String = "",
    val contenido: String = "",
)

fun ItemDetails.toItem(): Nota = Nota(
    id = id,
    name = name,
    description = contenido,
    fecha = fecha,
    contenido =  description
)


fun Nota.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

fun Nota.toItemDetails(): ItemDetails = ItemDetails(
    id = id,
    name = name,
    description = contenido,
    fecha = fecha,
    contenido = description
)
