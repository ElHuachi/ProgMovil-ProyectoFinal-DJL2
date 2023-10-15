package com.daviddj.proyecto_final_djl

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.viewModelFactory
import com.daviddj.proyecto_final_djl.model.Multimedia
import com.daviddj.proyecto_final_djl.model.Nota
import com.daviddj.proyecto_final_djl.model.NotasInfo
import com.daviddj.proyecto_final_djl.model.Tarea
import com.daviddj.proyecto_final_djl.model.TareasInfo
import com.daviddj.proyecto_final_djl.ui.theme.ProyectoFinalDJLTheme
import com.daviddj.proyecto_final_djl.viewModel.MainViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProyectoFinalDJLTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val tarea:Tarea = TareasInfo.tareas[1]
                    EditorTareas(tarea)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(modifier: Modifier =  Modifier){
    CenterAlignedTopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { /*TODO*/ }) {
                    Image(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(4.dp),
                        painter = painterResource(R.drawable.notas),
                        contentDescription = null
                    )
                    Text(
                        text = stringResource(R.string.notas),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(onClick = { /*TODO*/ }) {
                    Image(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(4.dp),
                        painter = painterResource(R.drawable.tareas),
                        contentDescription = null
                    )
                    Text(text = stringResource(R.string.tareas),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraBusqueda(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier
) {
    TextField(
        value = value,
        leadingIcon = {
            Icon(
                painter = painterResource(id = leadingIcon),
                null,
                modifier = Modifier.size(32.dp)
            )
        },
        singleLine = true,
        modifier = modifier
            .width(5.dp)
            .clip(RoundedCornerShape(10.dp)),
        onValueChange = onValueChanged,
        label = { Text(stringResource(label)) },
        keyboardOptions = keyboardOptions,
    )
}

@Composable
fun CheckboxWithText(
    text: String,
    isChecked: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    onCheckedImage: (Boolean) -> Unit
){
    var checkedState = remember { mutableStateOf(false) }
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ){
        Checkbox(
            checked = checkedState.value,
            onCheckedChange = {newValue ->
                checkedState.value = newValue
            },
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = Color.Gray
            )
        )
        Text(text = text)
    }
}

@Composable
fun TarjetaMultimedia(multimedia: Multimedia, modifier: Modifier = Modifier){
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .sizeIn(minHeight = 72.dp)
        ){
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(8.dp))
            ){
                Image(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(4.dp),
                    painter = painterResource(R.drawable.imagen),
                    contentDescription = null
                )
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier=Modifier.weight(1f)) {
                Text(
                    text = multimedia.description)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProyectoFinalDJLTheme(useDarkTheme=false) {
        //val nota : Nota = NotasInfo.notas[0]
        //EditorNotas(nota)
        //TareasList(tareas = TareasInfo.tareas)
        //NotasList(notas = NotasInfo.notas)

        val tarea:Tarea = TareasInfo.tareas[1]
        EditorTareas(tarea)
    }
}