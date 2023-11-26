package com.daviddj.proyecto_final_djl.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.daviddj.proyecto_final_djl.AppViewModelProvider
import com.daviddj.proyecto_final_djl.ComposeFileProvider
import com.daviddj.proyecto_final_djl.R
import com.daviddj.proyecto_final_djl.VideoPlayer
import com.daviddj.proyecto_final_djl.viewModel.NotaDetails
import com.daviddj.proyecto_final_djl.viewModel.TareaDetails
import com.daviddj.proyecto_final_djl.viewModel.TareaUiState
import com.daviddj.proyecto_final_djl.viewModel.TareasEditorViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import kotlin.reflect.KFunction2

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorTareas(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel : TareasEditorViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavHostController,
) {
    val coroutineScope = rememberCoroutineScope()
    var selectedDate by rememberSaveable { mutableStateOf("") }
    var selectedTime by rememberSaveable { mutableStateOf("") }
    var isDateSelected by rememberSaveable { mutableStateOf(false) }

    var imageUris by remember { mutableStateOf(listOf<Uri>()) }
    var videoUris by remember { mutableStateOf(listOf<Uri>()) }

    //MULTIMEDIA
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var videoUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                imageUris = imageUris.plus(uri!!)
                viewModel.imageUris=viewModel.imageUris.plus(uri!!)
            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success && imageUri != null) {
                imageUris = imageUris.plus(imageUri!!)
                viewModel.imageUris=viewModel.imageUris.plus(imageUri!!)
            }
        }
    )

    val videoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo(),
        onResult = { success ->
            if (success && videoUri != null) {
                videoUris = videoUris.plus(videoUri!!)
                viewModel.videoUris=viewModel.videoUris.plus(videoUri!!)
            }
        }
    )

    val context = LocalContext.current
    //MULTIMEDIA

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
            IconButton(onClick = {
                navController.navigate(Routes.TareasScreen.route)
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Regresar",
                    modifier = Modifier
                        .size(32.dp)
                        .padding(0.dp)
                )
            }
            //Spacer(modifier = Modifier.width(10.dp))
            TimePicker(onTimeSelected = {
                selectedTime = it
            },  isEnabled = isDateSelected)
            //Spacer(modifier = Modifier.width(10.dp))
            DatePicker(onDateSelected = {
                selectedDate = it
                isDateSelected = true
            })
            //Establecer recordatorio
//            IconButton(onClick = { /*TODO*/ }) {
//                Icon(
//                    imageVector = Icons.Filled.Done,
//                    contentDescription = "Crear Recordatorio",
//                    modifier = Modifier
//                        .size(32.dp)
//                        .padding(0.dp)
//                )
//            }

        }
        TareaEntryBody(
            tareaUiState = viewModel.tareaUiState,
            onTareaValueChange = { updatedTareaDetails ->
                val combinedDateTime = "$selectedDate $selectedTime"
                viewModel.updateUiState(updatedTareaDetails, combinedDateTime)
            },
            onSaveClick = {
                coroutineScope.launch {
                    val combinedDateTime = "$selectedDate $selectedTime"
                    viewModel.updateUiState(viewModel.tareaUiState.tareaDetails, combinedDateTime)
                    viewModel.saveTarea()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(5.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))        //MOSTRAR MULTIMEDIA
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    val uri = ComposeFileProvider.getImageUri(context)
                    imageUri = uri
                    cameraLauncher.launch(uri)
                }) {
                Image(
                    modifier = Modifier
                        .size(35.dp)
                        .padding(4.dp),
                    painter = painterResource(R.drawable.camara_fotografica),
                    contentDescription = null
                )
            }
            Button(
                onClick = {
                    val uri = ComposeFileProvider.getVideoUri(context)
                    videoUri = uri
                    videoLauncher.launch(uri)
                } ) {
                Image(
                    modifier = Modifier
                        .size(35.dp)
                        .padding(4.dp),
                    painter = painterResource(R.drawable.video),
                    contentDescription = null
                )
            }
            Button(onClick = { imagePicker.launch("image/*") }) {
                Image(
                    modifier = Modifier
                        .size(35.dp)
                        .padding(4.dp),
                    painter = painterResource(R.drawable.carpeta),
                    contentDescription = null
                )
            }
            Button(onClick = { /*TODO*/ }) {
                Image(
                    modifier = Modifier
                        .size(35.dp)
                        .padding(4.dp),
                    painter = painterResource(R.drawable.microfono),
                    contentDescription = null
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))//MOSTRAR MULTIMEDIA
        Box(
            modifier = modifier,
        ) {
            LazyColumn (modifier = Modifier.align(Alignment.Center)) {
                items(imageUris + videoUris) { uri ->
                    if (uri in imageUris) {
                        AsyncImage(
                            model = uri,
                            modifier = Modifier.height(400.dp).width(300.dp).align(Alignment.Center),
                            contentDescription = "Selected image",
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                    } else if (uri in videoUris) {
                        VideoPlayer(
                            videoUri = uri,
                            modifier = Modifier.height(400.dp).width(300.dp).align(Alignment.Center)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    //Text(text = "URI: $uri", modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TareaEntryBody(
    tareaUiState: TareaUiState,
    onTareaValueChange: (TareaDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Column() {
        TareaInputForm(
            tareaDetails = tareaUiState.tareaDetails,
            onValueChange = onTareaValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onSaveClick,
            enabled = tareaUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_action))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TareaInputForm(
    tareaDetails: TareaDetails,
    modifier: Modifier = Modifier,
    onValueChange: (TareaDetails) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
    ) {
        OutlinedTextField(
            value = tareaDetails.name,
            onValueChange = { onValueChange(tareaDetails.copy(name = it)) },
            label = { Text(stringResource(R.string.titulo)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        Text(
            text = currentDateTime,
            style = MaterialTheme.typography.bodySmall
        )
        OutlinedTextField(
            value = tareaDetails.contenido,
            onValueChange = { onValueChange(tareaDetails.copy(contenido = it)) },
            label = { Text(stringResource(R.string.contenido)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            //leadingIcon = { Text(Currency.getInstance(Locale.getDefault()).symbol) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = false
        )
    }
}



@Composable
fun DatePicker(onDateSelected: (String) -> Unit) {
    var fecha by rememberSaveable { mutableStateOf("") }
    val anio: Int
    val mes: Int
    val dia: Int
    val mCalendar: Calendar = Calendar.getInstance()
    anio = mCalendar.get(Calendar.YEAR)
    mes = mCalendar.get(Calendar.MONTH)
    dia = mCalendar.get(Calendar.DAY_OF_MONTH)

    val mDatePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, anio: Int, mes: Int, dia: Int ->
            fecha = "$anio-${mes + 1}-$dia"
            onDateSelected(fecha) // Llamar a la función de devolución de llamada con la fecha seleccionada
        },
        anio,
        mes,
        dia
    )

    Icon(
        imageVector = Icons.Filled.DateRange,
        contentDescription = "Recordatorios",
        modifier = Modifier
            .size(32.dp)
            .padding(0.dp)
            .clickable {
                mDatePickerDialog.show()
            }
    )
}


@Composable
fun TimePicker(onTimeSelected: (String) -> Unit, isEnabled: Boolean) {
    var hora by rememberSaveable { mutableStateOf("") }
    val mCalendar: Calendar = Calendar.getInstance()
    val hour = mCalendar[Calendar.HOUR_OF_DAY]
    val minute = mCalendar[Calendar.MINUTE]
    val second = mCalendar[Calendar.SECOND]

    val timePickerDialog = TimePickerDialog(
        LocalContext.current,
        { _, hourOfDay: Int, minuteOfDay: Int ->
            hora = String.format("%02d:%02d:%02d", hourOfDay, minuteOfDay, second)
            onTimeSelected(hora)
        }, hour, minute, false
    )

    if (isEnabled) {
        Icon(
            imageVector = Icons.Filled.Notifications,
            contentDescription = "Recordatorios",
            modifier = Modifier
                .size(32.dp)
                .padding(0.dp)
                .clickable {
                    timePickerDialog.show()
                }
        )
    } else {
        Icon(
            imageVector = Icons.Filled.Notifications,
            contentDescription = "Recordatorios (Deshabilitado)",
            modifier = Modifier
                .size(32.dp)
                .padding(0.dp)
                .alpha(0.5f) //Opacidad
        )
    }
}
