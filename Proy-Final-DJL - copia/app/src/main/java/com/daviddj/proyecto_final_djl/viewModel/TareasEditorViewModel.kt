package com.daviddj.proyecto_final_djl.viewModel

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.daviddj.proyecto_final_djl.data.TareasRepository
import com.daviddj.proyecto_final_djl.model.Tarea
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class TareasEditorViewModel(private val tareasRepository: TareasRepository): ViewModel() {

    var tareaUiState by mutableStateOf(TareaUiState())
        private set

    var imageUris by mutableStateOf(listOf<Uri>())
    var videoUris by mutableStateOf(listOf<Uri>())

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateUiState(tareaDetails: TareaDetails, selectedDate: String) {
        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val updatedTareaDetails = tareaDetails.copy(fecha = currentDateTime, fechaACompletar = selectedDate, imageUris = imageUris.joinToString(","), videoUris = videoUris.joinToString(","))
        tareaUiState = TareaUiState(tareaDetails = updatedTareaDetails, isEntryValid = validateInput(updatedTareaDetails))
    }

    private fun validateInput(uiState: TareaDetails = tareaUiState.tareaDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && contenido.isNotBlank() && fecha.isNotBlank()
        }
    }

    suspend fun saveTarea() {
        if (validateInput()) {
            tareasRepository.insertItem(tareaUiState.tareaDetails.toItem())
        }
    }
}

data class TareaUiState(
    val tareaDetails: TareaDetails = TareaDetails(),
    val isEntryValid: Boolean = false
)

data class TareaDetails(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    var fecha: String = "",
    var fechaACompletar: String = "",
    val contenido: String = "",
    val isComplite: Boolean = false,
    val imageUris: String = "",
    val videoUris: String = ""
)

fun TareaDetails.toItem(): Tarea = Tarea(
    id = id,
    name = name,
    description = description,
    fecha = fecha,
    fechaACompletar = fechaACompletar,
    contenido = contenido,
    isComplete = isComplite,
    imageUris = imageUris,
    videoUris = videoUris
)

fun Tarea.toItemUiState(isEntryValid: Boolean = false): TareaUiState = TareaUiState(
    tareaDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

fun Tarea.toItemDetails(): TareaDetails = TareaDetails(
    id = id,
    name = name,
    description = description,
    fecha = fecha,
    fechaACompletar = fechaACompletar,
    contenido = contenido,
    isComplite = isComplete,
    imageUris = imageUris,
    videoUris = videoUris
)