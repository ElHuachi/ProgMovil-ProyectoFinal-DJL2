package com.daviddj.proyecto_final_djl.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    private fun updateAppState() {
        _uiState.update { currentState ->
            currentState.copy(
            )
        }
    }

    private fun updateNotas(){

    }

    private fun updateTareas(){

    }
}