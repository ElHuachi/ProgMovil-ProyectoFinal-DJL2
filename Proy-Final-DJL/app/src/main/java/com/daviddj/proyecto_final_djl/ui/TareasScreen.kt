package com.daviddj.proyecto_final_djl.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.daviddj.proyecto_final_djl.AppTopBar
import com.daviddj.proyecto_final_djl.AppViewModelProvider
import com.daviddj.proyecto_final_djl.BarraBusqueda
import com.daviddj.proyecto_final_djl.CustomTopAppBar
import com.daviddj.proyecto_final_djl.R
import com.daviddj.proyecto_final_djl.model.Tarea
import com.daviddj.proyecto_final_djl.viewModel.TareasScreenViewModel

@Composable
fun TareaCard(tarea: Tarea, modifier: Modifier){
    val checkedState = remember { mutableStateOf(false) }
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier,
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .sizeIn(minHeight = 72.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = tarea.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(text = tarea.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(text = tarea.fecha.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Column(modifier=Modifier.padding(16.dp)) {
                Checkbox(checked = checkedState.value,
                    onCheckedChange = { newValue -> checkedState.value = newValue },
                    colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = Color.Gray
                    )
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TareasList(
    modifier: Modifier = Modifier,
    tareas: List<Tarea>,
    windowSize: WindowWidthSizeClass,
    navigateToItemUpdate: (Int) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    navController: NavHostController,
    appViewModel : TareasScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    var busquedaInput by remember { mutableStateOf("") }
    val homeUiState by appViewModel.tareaUiState.collectAsState()
    Scaffold(
        topBar = {
            Column {
                CustomTopAppBar("Tareas")
                AppTopBar(navController = navController)
                Spacer(modifier = Modifier.height(15.dp))
                Row (modifier = Modifier.align(Alignment.CenterHorizontally)){
                    BarraBusqueda(
                        label = R.string.busqueda,
                        leadingIcon = R.drawable.lupa,
                        value = appViewModel.busquedaInput.value,
                        onValueChanged = { appViewModel.busquedaInput.value = it },
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
            FloatingActionButton(onClick = {navController.navigate(Routes.TareasEditor.route)}) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ){ innerPadding ->
        TareaBody(
            itemList = homeUiState.itemList,
            onItemClick = navigateToItemUpdate ,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}


@Composable
private fun TareaBody(
    itemList: List<Tarea>, onItemClick: (Int) -> Unit, modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (itemList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_item_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            InventoryList(
                itemList = itemList,
                onItemClick = { onItemClick(it.id) },
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
private fun InventoryList(
    itemList: List<Tarea>, onItemClick: (Tarea) -> Unit, modifier: Modifier = Modifier
) {


    LazyColumn(modifier = modifier) {

        items(items = itemList, key = { it.id }) { item ->
            InventoryItem(item = item,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onItemClick(item) })
        }
    }
}


@Composable
private fun InventoryItem(
    item: Tarea, modifier: Modifier = Modifier
) {
    val checkedState = remember { mutableStateOf(false) }
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier,
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .sizeIn(minHeight = 72.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(text = item.fecha.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Column(modifier=Modifier.padding(16.dp)) {
                Checkbox(checked = checkedState.value,
                    onCheckedChange = { newValue -> checkedState.value = newValue },
                    colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = Color.Gray
                    )
                )
            }
        }
    }
}