package com.daviddj.proyecto_final_djl.viewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AppUiState (
    val cantidadNotas: Int=0,
    val cantidadTareas: Int=0,
    val cantidadTareasComp : Int=0
)