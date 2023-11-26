package com.daviddj.proyecto_final_djl.viewModel

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.daviddj.proyecto_final_djl.data.NotasRepository
import com.daviddj.proyecto_final_djl.model.Nota
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class NotasEditorViewModel(private val notasRepository: NotasRepository) : ViewModel() {

    var notaUiState by mutableStateOf(NotaUiState())
        private set

    var imageUris by mutableStateOf(listOf<Uri>())
    var videoUris by mutableStateOf(listOf<Uri>())

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateUiState(notaDetails: NotaDetails) {
        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val updatedNotaDetails = notaDetails.copy(fecha = currentDateTime, imageUris = imageUris.joinToString(","), videoUris = videoUris.joinToString(","))
        notaUiState = NotaUiState(notaDetails = updatedNotaDetails, isEntryValid = validateInput(updatedNotaDetails))
    }

    private fun validateInput(uiState: NotaDetails = notaUiState.notaDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()  && contenido.isNotBlank() && fecha.isNotBlank()
        }
    }

    suspend fun saveNota() {
        if (validateInput()) {
            notasRepository.insertItem(notaUiState.notaDetails.toItem())
        }
    }
}

data class NotaUiState(
    val notaDetails: NotaDetails = NotaDetails(),
    val isEntryValid: Boolean = false
)

data class NotaDetails(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    var fecha: String = "",
    val contenido: String = "",
    val imageUris: String = "",
    val videoUris: String = ""
)

fun NotaDetails.toItem(): Nota = Nota(
    id = id,
    name = name,
    description = description,
    fecha = fecha,
    contenido = contenido,
    imageUris = imageUris,
    videoUris = videoUris
)

fun Nota.toItemUiState(isEntryValid: Boolean = false): NotaUiState = NotaUiState(
    notaDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

fun Nota.toItemDetails(): NotaDetails = NotaDetails(
    id = id,
    name = name,
    description = description,
    fecha = fecha,
    contenido = contenido,
    imageUris = imageUris,
    videoUris = videoUris
)
