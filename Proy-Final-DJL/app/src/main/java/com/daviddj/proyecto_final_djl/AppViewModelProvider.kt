package com.daviddj.proyecto_final_djl

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.test.core.app.ApplicationProvider
import com.daviddj.proyecto_final_djl.notaDetails.ItemDetailsViewModel
import com.daviddj.proyecto_final_djl.notasDetails.ItemEntryViewModel
import com.daviddj.proyecto_final_djl.notasDetails.NotaEditViewModel
import com.daviddj.proyecto_final_djl.tareasDetails.TareaDetailsViewModel
import com.daviddj.proyecto_final_djl.tareasDetails.TareaEntryViewModel
import com.daviddj.proyecto_final_djl.tareasDetails.TareasEditViewModel
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
            TareasScreenViewModel(notasApplication().container.tareasRepository)
        }
        // Initializer for ItemEditViewModel
        initializer {
            NotaEditViewModel(
                this.createSavedStateHandle(),
                notasApplication().container.notasRepository
            )
        }
        // Initializer for ItemEntryViewModel
        initializer {
            ItemEntryViewModel(notasApplication().container.notasRepository)
        }

        // Initializer for ItemDetailsViewModel
        initializer {
            ItemDetailsViewModel(
                this.createSavedStateHandle(),
                notasApplication().container.notasRepository
            )
        }

        // Initializer for ItemEditViewModel
        initializer {
            TareasEditViewModel(
                this.createSavedStateHandle(),
                notasApplication().container.tareasRepository
            )
        }

        // Initializer for ItemEntryViewModel
        initializer {
            TareaEntryViewModel(notasApplication().container.tareasRepository)
        }

        // Initializer for ItemDetailsViewModel
        initializer {
            TareaDetailsViewModel(
                this.createSavedStateHandle(),
                notasApplication().container.tareasRepository
            )
        }
    }
}

fun CreationExtras.notasApplication(): NotasApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as NotasApplication)