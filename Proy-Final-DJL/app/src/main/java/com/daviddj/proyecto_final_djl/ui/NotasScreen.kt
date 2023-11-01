package com.daviddj.proyecto_final_djl.ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.daviddj.proyecto_final_djl.model.Nota
import androidx.navigation.NavHostController
import com.daviddj.proyecto_final_djl.AppTopBar
import com.daviddj.proyecto_final_djl.BarraBusqueda
import com.daviddj.proyecto_final_djl.CustomTopAppBar
import com.daviddj.proyecto_final_djl.R
import com.daviddj.proyecto_final_djl.utils.NotasContentType
import com.daviddj.proyecto_final_djl.utils.NotasNavigationType
import com.daviddj.proyecto_final_djl.viewModel.NotasScreenViewModel

@Composable
fun NotaCard(
    nota: Nota,
    modifier: Modifier = Modifier){
    val navigationItemContentList = listOf(
        NavigationItemContent(
            title = "Notas",
            icon = { Icon(Icons.Default.Add, contentDescription = "Agregar") },
            route = Routes.NotasEditor.route
        )
    )


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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotasList(
    modifier: Modifier = Modifier,
    windowSize: WindowWidthSizeClass,
    notas: List<Nota>,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    appViewModel : NotasScreenViewModel = viewModel(),
    navController: NavHostController
){
    val navigationType: NotasNavigationType
    val contentType: NotasContentType

    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            navigationType = NotasNavigationType.BOTTOM_NAVIGATION
            contentType = NotasContentType.LIST_ONLY
        }
        WindowWidthSizeClass.Medium -> {
            navigationType = NotasNavigationType.NAVIGATION_RAIL
            contentType = NotasContentType.LIST_ONLY
        }
        WindowWidthSizeClass.Expanded -> {
            navigationType = NotasNavigationType.PERMANENT_NAVIGATION_DRAWER
            contentType = NotasContentType.LIST_AND_DETAIL
        }
        else -> {
            navigationType = NotasNavigationType.BOTTOM_NAVIGATION
            contentType = NotasContentType.LIST_ONLY
        }
    }

    Scaffold(
        topBar = {
            Column {
                CustomTopAppBar("Notas")
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
            FloatingActionButton(onClick = { navController.navigate(Routes.NotasEditor.route) }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ){ it ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(contentPadding = it) {
                itemsIndexed(notas){index, nota ->
                    NotaCard(
                        nota = nota,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

private data class NavigationItemContent(
    val title: String,
    val icon: @Composable () -> Unit,
    val route: String
)