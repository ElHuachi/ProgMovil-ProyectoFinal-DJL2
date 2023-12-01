package com.daviddj.proyecto_final_djl.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.daviddj.proyecto_final_djl.model.Nota
import androidx.navigation.NavHostController
import com.daviddj.proyecto_final_djl.AppTopBar
import com.daviddj.proyecto_final_djl.AppViewModelProvider
import com.daviddj.proyecto_final_djl.BarraBusqueda
import com.daviddj.proyecto_final_djl.CustomTopAppBar
import com.daviddj.proyecto_final_djl.R
import com.daviddj.proyecto_final_djl.utils.NotasContentType
import com.daviddj.proyecto_final_djl.utils.NotasNavigationType
import com.daviddj.proyecto_final_djl.viewModel.NotasScreenViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotasList(
    modifier: Modifier = Modifier,
    windowSize: WindowWidthSizeClass,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel : NotasScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavHostController,
    navigateToItemUpdate: (Int) -> Unit,
){
    val homeUiState by viewModel.homeUiState.collectAsState()
    Scaffold(
        topBar = {
            Column {
                CustomTopAppBar(stringResource(R.string.notas))
                AppTopBar(navController = navController)
                Spacer(modifier = Modifier.height(15.dp))
                Row (modifier = Modifier.align(Alignment.CenterHorizontally)){
                    BarraBusqueda(
                        label = R.string.busqueda,
                        leadingIcon = R.drawable.lupa,
                        value = viewModel.busquedaInput.value,
                        onValueChanged = { viewModel.busquedaInput.value = it },
                        modifier = Modifier
                            .padding(bottom = 32.dp)
                            .fillMaxWidth(.925f),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        )
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Routes.NotasEditor.route) }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ){ innerPadding ->
        HomeBody(
            notaList = homeUiState.itemList,
            onNotaClick = navigateToItemUpdate,
            busquedaInput = viewModel.busquedaInput.value,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
private fun HomeBody(
    notaList: List<Nota>,
    onNotaClick: (Int) -> Unit,
    busquedaInput: String,
    modifier: Modifier = Modifier
) {
    // Filtrar las notas si hay texto en la barra de búsqueda
    val notasFiltradas = notaList.filter { nota ->
        busquedaInput.isEmpty() || nota.name.contains(busquedaInput, ignoreCase = true)
    }

    // Ordenar las notas por fecha de forma descendente
    val notasOrdenadas = notasFiltradas.sortedByDescending { it.fecha }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (notasFiltradas.isEmpty()) {
            Text(
                text = stringResource(R.string.no_item_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            NotasList(
                notaList = notasOrdenadas,
                onNotaClick = { onNotaClick(it.id) },
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }
    }
}

@Composable
private fun NotasList(
    notaList: List<Nota>, onNotaClick: (Nota) -> Unit, modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = notaList, key = { it.id }) { nota ->
            InventoryNota(nota = nota,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable { onNotaClick(nota) })
        }
    }
}

@Composable
private fun InventoryNota(
    nota: Nota, modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .sizeIn(minHeight = 72.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = nota.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = nota.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = nota.fecha.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


private data class NavigationItemContent(
    val title: String,
    val icon: @Composable () -> Unit,
    val route: String
)