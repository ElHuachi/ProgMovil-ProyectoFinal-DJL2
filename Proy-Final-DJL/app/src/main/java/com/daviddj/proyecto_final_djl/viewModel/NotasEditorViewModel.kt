package com.daviddj.proyecto_final_djl.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.daviddj.proyecto_final_djl.model.Nota
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime

class NotasEditorViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    val text = mutableStateOf(TextFieldValue())
    val titulo = mutableStateOf("")
    @RequiresApi(Build.VERSION_CODES.O)
    val fecha = mutableStateOf(LocalDateTime.now())

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadNota(id: Int, notas: List<Nota>) {
        val nota = notas.find { it.id == id }
        titulo.value = nota?.name ?: ""
        text.value = TextFieldValue(nota?.contenido ?: "")
        fecha.value = (nota?.fecha ?: LocalDateTime.now()) as LocalDateTime?
    }
}