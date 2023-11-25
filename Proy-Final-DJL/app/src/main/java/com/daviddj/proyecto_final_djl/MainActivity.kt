package com.daviddj.proyecto_final_djl

import android.content.res.Configuration
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.daviddj.proyecto_final_djl.model.Multimedia
import com.daviddj.proyecto_final_djl.model.NotasInfo
import com.daviddj.proyecto_final_djl.model.TareasInfo
import com.daviddj.proyecto_final_djl.navigation.InventoryNavHost
import com.daviddj.proyecto_final_djl.ui.EditorNotas
import com.daviddj.proyecto_final_djl.ui.EditorTareas
import com.daviddj.proyecto_final_djl.ui.NotasList
import com.daviddj.proyecto_final_djl.ui.Routes
import com.daviddj.proyecto_final_djl.ui.TareasList
import com.daviddj.proyecto_final_djl.ui.theme.ProyectoFinalDJLTheme
import com.daviddj.proyecto_final_djl.viewModel.NotaDetails
import com.daviddj.proyecto_final_djl.viewModel.TareaDetails

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
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
                    val windowSize = calculateWindowSizeClass(this)
                    val navigationcontroller = rememberNavController()

                    InventoryNavHost(navController = navigationcontroller, windowSize = windowSize )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(title: String, ) {
    TopAppBar(
        title = { Text(text = title) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(modifier: Modifier =  Modifier, navController: NavController){
    CenterAlignedTopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { navController.navigate(Routes.NotasScreen.route) }) {
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
                Button(onClick = { navController.navigate(Routes.TareasScreen.route) }) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraTitulo(
    @StringRes label: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier
) {
    TextField(
        value = value,
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
fun DefaultPreview() {
    ProyectoFinalDJLTheme {
        Surface {
            NotasList(notas = NotasInfo.notas, navController = rememberNavController(), windowSize = WindowWidthSizeClass.Medium)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, widthDp = 700)
@Composable
fun GreetingPreview() {
    ProyectoFinalDJLTheme(useDarkTheme=false) {
        Surface {
            NotasList(notas = NotasInfo.notas, navController = rememberNavController(), windowSize = WindowWidthSizeClass.Medium)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, widthDp = 1000)
@Composable
fun GreetingExpandedPreview() {
    ProyectoFinalDJLTheme(useDarkTheme=false) {
        Surface {
            NotasList(notas = NotasInfo.notas, navController = rememberNavController(), windowSize = WindowWidthSizeClass.Expanded)
        }
    }
}