package com.daviddj.proyecto_final_djl.viewModel

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daviddj.proyecto_final_djl.data.NotasRepository
import com.daviddj.proyecto_final_djl.ui.NotaEditDestination
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UpdateNotaViewModel(
    savedStateHandle: SavedStateHandle,
    private val notasRepository: NotasRepository
) : ViewModel() {

    var imageUris by mutableStateOf(listOf<Uri>())
    var videoUris by mutableStateOf(listOf<Uri>())

    var notaUiState by mutableStateOf(NotaUiState())
        private set

    private val notaId: Int = checkNotNull(savedStateHandle[NotaEditDestination.itemIdArg])

    init {
        viewModelScope.launch {
            notaUiState = notasRepository.getItemStream(notaId)
                .filterNotNull()
                .first()
                .toItemUiState(true)
        }
    }

    /**
     * Update the item in the [NotasRepository]'s data source
     */
    suspend fun updateItem() {
        if (validateInput(notaUiState.notaDetails)) {
            notasRepository.updateItem(notaUiState.notaDetails.toItem())
        }
    }

    /**
     * Updates the [notaUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    @SuppressLint("NewApi")
    fun updateUiState(itemDetails: NotaDetails) {
        itemDetails.fecha  = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val updatedNotaDetails = itemDetails.copy(fecha = itemDetails.fecha, imageUris = imageUris.joinToString(","), videoUris = videoUris.joinToString(","))
        notaUiState =
            NotaUiState(notaDetails = updatedNotaDetails, isEntryValid = validateInput(itemDetails))
    }

    private fun validateInput(uiState: NotaDetails = notaUiState.notaDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && fecha.isNotBlank() && contenido.isNotBlank()
        }
    }
}
