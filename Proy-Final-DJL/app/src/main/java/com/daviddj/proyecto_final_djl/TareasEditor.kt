package com.daviddj.proyecto_final_djl

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.daviddj.proyecto_final_djl.model.Tarea
import com.daviddj.proyecto_final_djl.viewModel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorTareas(tarea: Tarea,modifier: Modifier = Modifier, appViewModel : MainViewModel = viewModel()) {
    val appUiState by appViewModel.uiState.collectAsState()
    var checkedState = remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(TextFieldValue()) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowLeft,
                    contentDescription = "Regresar",
                    modifier = Modifier
                        .size(32.dp)
                        .padding(0.dp)
                )
            }
            Spacer(modifier = Modifier.width(40.dp))
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Recordatorios",
                    modifier = Modifier
                        .size(32.dp)
                        .padding(0.dp)
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Recordatorios",
                    modifier = Modifier
                        .size(32.dp)
                        .padding(0.dp)
                )
            }
            CheckboxWithText(
                text = "Completado",
                isChecked = checkedState,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 8.dp)
            ) { var isChecked = it}
        }
        Text(
            text = tarea.name,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = tarea.fecha.toString(),
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = text,
            onValueChange = {
                text = it
            },
            singleLine = false,
            modifier = Modifier
                .background(Color.White)
                .border(0.dp, Color.White)
                .fillMaxWidth(1f)
                .align(Alignment.CenterHorizontally),
            textStyle = TextStyle(fontSize = 20.sp, color = Color.Black)
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Button(onClick = { /*TODO*/ }) {
                Image(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(4.dp),
                    painter = painterResource(R.drawable.camara_fotografica),
                    contentDescription = null
                )
            }
            Button(onClick = { /*TODO*/ }) {
                Image(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(4.dp),
                    painter = painterResource(R.drawable.carpeta),
                    contentDescription = null
                )
            }
            Button(onClick = { /*TODO*/ }) {
                Image(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(4.dp),
                    painter = painterResource(R.drawable.microfono),
                    contentDescription = null
                )
            }
        }
    }
}