package com.daviddj.proyecto_final_djl

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.test.core.app.ApplicationProvider
import com.daviddj.proyecto_final_djl.viewModel.NotaDetailsViewModel
import com.daviddj.proyecto_final_djl.viewModel.NotasEditorViewModel
import com.daviddj.proyecto_final_djl.viewModel.NotasScreenViewModel
import com.daviddj.proyecto_final_djl.viewModel.TareaDetailsViewModel
import com.daviddj.proyecto_final_djl.viewModel.TareasEditorViewModel
import com.daviddj.proyecto_final_djl.viewModel.TareasScreenViewModel
import com.daviddj.proyecto_final_djl.viewModel.UpdateNotaViewModel
import com.daviddj.proyecto_final_djl.viewModel.UpdateTareaViewModel

object AppViewModelProvider{
    @RequiresApi(Build.VERSION_CODES.O)
    val Factory = viewModelFactory {
        initializer {
            NotasEditorViewModel(notasApplication().container.notasRepository,
                notasApplication().container.notasMultimediaRepository
            )
        }

        initializer {
            TareasEditorViewModel(notasApplication().container.tareasRepository,
                notasApplication().container.tareasMultimediaRepository
            )
        }

        initializer {
            NotasScreenViewModel(notasApplication().container.notasRepository)
        }

        initializer {
            TareasScreenViewModel(notasApplication().container.tareasRepository)
        }

        initializer {
            NotaDetailsViewModel(
                this.createSavedStateHandle(),
                notasApplication().container.notasRepository
            )
        }

        initializer {
            UpdateNotaViewModel(
                this.createSavedStateHandle(),
                notasApplication().container.notasRepository,
                notasApplication().container.notasMultimediaRepository
            )
        }

        initializer {
            TareaDetailsViewModel(
                this.createSavedStateHandle(),
                notasApplication().container.tareasRepository
            )
        }

        initializer {
            UpdateTareaViewModel(
                this.createSavedStateHandle(),
                notasApplication().container.tareasRepository,
                notasApplication().container.tareasMultimediaRepository
            )
        }
    }
}

fun CreationExtras.notasApplication(): NotasApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as NotasApplication)