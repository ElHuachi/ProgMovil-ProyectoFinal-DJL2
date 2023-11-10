package com.daviddj.proyecto_final_djl.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daviddj.proyecto_final_djl.data.NotasRepository
import com.daviddj.proyecto_final_djl.data.TareasRepository
import com.daviddj.proyecto_final_djl.model.Nota
import com.daviddj.proyecto_final_djl.model.NotasInfo
import com.daviddj.proyecto_final_djl.model.Tarea
import com.daviddj.proyecto_final_djl.model.TareasInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class TareasEditorViewModel(private val tareasRepository: TareasRepository): ViewModel() {

    var tareaUiState by mutableStateOf(TareaUiState())
        private set

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateUiState(tareaDetails: TareaDetails) {
        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val updatedTareaDetails = tareaDetails.copy(fecha = currentDateTime)
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
    val fecha: String = "",
    val contenido: String = "",
    val isComplite: Boolean = false
)

fun TareaDetails.toItem(): Tarea = Tarea(
    id = id,
    name = name,
    description = description,
    fecha = fecha,
    contenido = contenido,
    isComplete = isComplite
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
    contenido = contenido,
    isComplite = isComplete
)