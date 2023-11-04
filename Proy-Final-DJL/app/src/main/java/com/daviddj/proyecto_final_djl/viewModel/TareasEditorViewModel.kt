package com.daviddj.proyecto_final_djl.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.daviddj.proyecto_final_djl.model.Tarea
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime

class TareasEditorViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    val text = mutableStateOf(TextFieldValue())
    val titulo = mutableStateOf("")
    @RequiresApi(Build.VERSION_CODES.O)
    val fecha = mutableStateOf(LocalDateTime.now())
    var tareaCargada = false

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadTarea(id: Int, tareas: List<Tarea>) {
        val tarea = tareas.find { it.id == id }
        titulo.value = tarea?.name ?: ""
        text.value = TextFieldValue(tarea?.contenido ?: "")
        fecha.value = (tarea?.fecha ?: LocalDateTime.now()) as LocalDateTime?
    }
}