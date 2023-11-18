package com.daviddj.proyecto_final_djl

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.test.core.app.ApplicationProvider
import com.daviddj.proyecto_final_djl.viewModel.NotasEditorViewModel
import com.daviddj.proyecto_final_djl.viewModel.NotasScreenViewModel
import com.daviddj.proyecto_final_djl.viewModel.TareasEditorViewModel
import com.daviddj.proyecto_final_djl.viewModel.TareasScreenViewModel

object AppViewModelProvider{
    @RequiresApi(Build.VERSION_CODES.O)
    val Factory = viewModelFactory {
        initializer {
            NotasEditorViewModel(notasApplication().container.notasRepository)
        }

        initializer {
            TareasEditorViewModel(notasApplication().container.tareasRepository)
        }

        initializer {
            NotasScreenViewModel(notasApplication().container.notasRepository)
        }

        initializer {
            TareasScreenViewModel()
        }
    }
}

fun CreationExtras.notasApplication(): NotasApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as NotasApplication)