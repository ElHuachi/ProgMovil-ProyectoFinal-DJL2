package com.daviddj.proyecto_final_djl.ui

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.daviddj.proyecto_final_djl.AppViewModelProvider
import com.daviddj.proyecto_final_djl.ComposeFileProvider
import com.daviddj.proyecto_final_djl.InventoryTopAppBar
import com.daviddj.proyecto_final_djl.NavigationDestination2
import com.daviddj.proyecto_final_djl.R
import com.daviddj.proyecto_final_djl.VideoPlayer
import com.daviddj.proyecto_final_djl.viewModel.NotasEditorViewModel
import com.daviddj.proyecto_final_djl.viewModel.TareasEditorViewModel
import com.daviddj.proyecto_final_djl.viewModel.UpdateTareaViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.launch
import java.time.LocalDateTime

object TareaEditDestination : NavigationDestination2 {
    override val route = "tarea_edit"
    override val titleRes = R.string.edit_item_title2
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun UpdateTareaScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdateTareaViewModel = viewModel(factory = AppViewModelProvider.Factory),
    alarmScheduler: AlarmScheduler,
    viewModel2: TareasEditorViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onClickStGra: () -> Unit,
    onClickSpGra: () -> Unit,
    onClickStRe: () -> Unit,
    onClickSpRe: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var selectedDate by rememberSaveable { mutableStateOf("") }
    var selectedTime by rememberSaveable { mutableStateOf("") }
    var isDateSelected by rememberSaveable { mutableStateOf(false) }
    var isTimeSelected by rememberSaveable { mutableStateOf(false) }

    var imageUris by remember { mutableStateOf(listOf<Uri>()) }
    var videoUris by remember { mutableStateOf(listOf<Uri>()) }

    //MULTIMEDIA
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var videoUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var audioUri by remember {
        mutableStateOf<Uri?>(null)
    }

    //AUDIO
    val recordAudioPermissionState = rememberPermissionState(
        Manifest.permission.RECORD_AUDIO
    )

    //Realiza un seguimiento del estado del diálogo de justificación, necesario cuando el usuario requiere más justificación
    var rationaleState by remember {
        mutableStateOf<RationaleState?>(null)
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

    //ALARMA
    var secondText by remember {
        mutableStateOf("")
    }
    var messageText by remember {
        mutableStateOf("")
    }
    var alarmItem : AlarmItem? = null
    //ALARMA

    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(TareaEditDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.height(55.dp))
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                TimePicker(onTimeSelected = {
                    selectedTime = it
                    isTimeSelected = true
                }, isEnabled = isDateSelected)
                Spacer(modifier = Modifier.width(10.dp))
                DatePicker(onDateSelected = {
                    selectedDate = it
                    isDateSelected=true
                })
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.recordatorio),fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    viewModel.updateMensaje(viewModel.tareaUiState.tareaDetails.name)
                    alarmItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        AlarmItem(
                            LocalDateTime.now().plusSeconds(1000),
                            viewModel.mensaje.value,
                            selectedDate,
                            selectedTime
                        )
                    } else {
                        TODO("VERSION.SDK_INT < O")
                    }
                    alarmItem?.let(alarmScheduler::schedule)
                    secondText = ""
                    messageText = ""

                }, enabled = isTimeSelected) {
                    Text(text = stringResource(R.string.reminder))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    alarmItem?.let(alarmScheduler::cancel)
                }, enabled = isTimeSelected) {
                    Text(text = stringResource(R.string.cancelar))
                }
                Spacer(modifier = Modifier.width(8.dp))
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
                        viewModel.updateItem()
                        navigateBack()
                    }
                },
                modifier = Modifier.padding(innerPadding)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        val uri = ComposeFileProvider.getImageUri(context)
                        imageUri = uri
                        cameraLauncher.launch(uri)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)) {
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
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)) {
                    Image(
                        modifier = Modifier
                            .size(35.dp)
                            .padding(4.dp),
                        painter = painterResource(R.drawable.video),
                        contentDescription = null
                    )
                }
                Button(onClick = { imagePicker.launch("image/*") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)) {
                    Image(
                        modifier = Modifier
                            .size(35.dp)
                            .padding(4.dp),
                        painter = painterResource(R.drawable.carpeta),
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))        //MOSTRAR MULTIMEDIA
            Row{
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    rationaleState?.run { PermissionRationaleDialog(rationaleState = this) }
                    var viewM: NotasEditorViewModel
                    PermissionRequestButtonTareas(
                        isGranted = recordAudioPermissionState.status.isGranted,
                        title = stringResource(R.string.record_audio),
                        onClickStGra,
                        onClickSpGra,
                        viewModel.audioUris,
                        viewModel2
                    ){
                        if (recordAudioPermissionState.status.shouldShowRationale) {
                            rationaleState = RationaleState(
                                "Permiso para grabar audio",
                                "In order to use this feature please grant access by accepting " + "the grabar audio dialog." + "\n\nWould you like to continue?",
                            ) { proceed ->
                                if (proceed) {
                                    recordAudioPermissionState.launchPermissionRequest()
                                }
                                rationaleState = null
                            }
                        } else {
                            recordAudioPermissionState.launchPermissionRequest()
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))        //MOSTRAR MULTIMEDIA
            val imagenesCargadas = viewModel.tareaUiState.tareaDetails.imageUris.split(",")
            val videosCargados = viewModel.tareaUiState.tareaDetails.videoUris.split(",")
            val audiosCargados = viewModel.tareaUiState.tareaDetails.audioUris.split(",")
            val nuevos = imageUris + videoUris

            val combinedList = listOf(imagenesCargadas, videosCargados, audiosCargados, nuevos)

            LazyColumn {
                itemsIndexed(combinedList) { index, list ->
                    when (index) {
                        0 -> { // Renderiza las imágenes cargadas aquí
                            list.forEach { uri ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                ) {
                                    Column {
                                        val parsedUri = Uri.parse(uri.toString())
                                        AsyncImage(
                                            model = parsedUri,
                                            modifier = Modifier
                                                .height(400.dp)
                                                .fillMaxWidth()
                                                .align(Alignment.CenterHorizontally),
                                            contentDescription = "Selected image",
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                        // Agrega el botón aquí
                                        Button(
                                            onClick = {
                                                // Elimina la tarjeta y quita la imagen del arreglo.
                                                imageUris = imageUris.filter { it != uri }
                                                videoUris = videoUris.filter { it != uri }
                                                val u = Uri.parse(uri.toString())
                                                viewModel.removeUri(u)
                                            },
                                            modifier = Modifier.align(Alignment.End)
                                        ) {
                                            //Text("Eliminar")
                                            Image(
                                                modifier = Modifier
                                                    .size(25.dp)
                                                    .padding(2.dp),
                                                painter = painterResource(R.drawable.eliminar),
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        1 -> { // Renderiza los videos cargados aquí
                            list.forEach { uri ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                ) {
                                    Column {
                                        val parsedUri = Uri.parse(uri.toString())
                                        VideoPlayer(
                                            videoUri = parsedUri,
                                            modifier = Modifier
                                                .height(400.dp)
                                                .fillMaxWidth()
                                                .align(Alignment.CenterHorizontally)
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                        // Agrega el botón aquí
                                        Button(
                                            onClick = {
                                                // Elimina la tarjeta y quita la imagen del arreglo.
                                                imageUris = imageUris.filter { it != uri }
                                                videoUris = videoUris.filter { it != uri }
                                                val u = Uri.parse(uri.toString())
                                                viewModel.removeUri(u)
                                            },
                                            modifier = Modifier.align(Alignment.End)
                                        ) {
                                            //Text("Eliminar")
                                            Image(
                                                modifier = Modifier
                                                    .size(25.dp)
                                                    .padding(2.dp),
                                                painter = painterResource(R.drawable.eliminar),
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        2 -> { //AUDIOS
                            list.forEach { uri ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                ) {
                                    Column {
                                        Row{
                                            val audioPlayer2 = AndroidAudioPlayer2(context,Uri.parse(uri.toString()))
                                            PermissionRequestButton2(
                                                isGranted = recordAudioPermissionState.status.isGranted,
                                                title = stringResource(R.string.record_audio),
                                                onClickSpGra,
                                                onClickStRe,
                                                onClickStRe = {
                                                    audioPlayer2.start(Uri.parse(uri.toString()))
                                                },
                                                onClickSpRe = {
                                                    audioPlayer2.stop()
                                                },
                                                viewModel.audioUris,
                                            ){
                                                if (recordAudioPermissionState.status.shouldShowRationale) {
                                                    rationaleState = RationaleState(
                                                        "Permiso para grabar audio",
                                                        "In order to use this feature please grant access by accepting " + "the grabar audio dialog." + "\n\nWould you like to continue?",
                                                    ) { proceed ->
                                                        if (proceed) {
                                                            recordAudioPermissionState.launchPermissionRequest()
                                                        }
                                                        rationaleState = null
                                                    }
                                                } else {
                                                    recordAudioPermissionState.launchPermissionRequest()
                                                }
                                            }
                                        }
                                        //Spacer(modifier = Modifier.height(16.dp))
                                        Button(
                                            onClick = {
                                                // Elimina la tarjeta y quita la imagen del arreglo.
                                                imageUris = imageUris.filter { it != uri }
                                                videoUris = videoUris.filter { it != uri }
                                                val u = Uri.parse(uri.toString())
                                                viewModel.removeUri(u)
                                            },
                                            modifier = Modifier.align(Alignment.End)
                                        ) {
                                            //Text(stringResource(R.string.delete))
                                            Image(
                                                modifier = Modifier
                                                    .size(25.dp)
                                                    .padding(2.dp),
                                                painter = painterResource(R.drawable.eliminar),
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        3 -> { // Renderiza los nuevos elementos aquí
                            Text(text = "Nuevos elementos", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(16.dp))
                            list.forEach { uri ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                ) {
                                    Column {
                                        if (uri in imageUris) {
                                            AsyncImage(
                                                model = uri,
                                                modifier = Modifier
                                                    .height(400.dp)
                                                    .fillMaxWidth()
                                                    .align(CenterHorizontally),
                                                contentDescription = "Selected image",
                                            )
                                        } else if (uri in videoUris) {
                                            val parsedUri = Uri.parse(uri.toString())
                                            VideoPlayer(
                                                videoUri = parsedUri,
                                                modifier = Modifier
                                                    .height(400.dp)
                                                    .fillMaxWidth()
                                                    .align(CenterHorizontally)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Button(
                                            onClick = {
                                                // Elimina la tarjeta y quita la imagen del arreglo.
                                                imageUris = imageUris.filter { it != uri }
                                                videoUris = videoUris.filter { it != uri }
                                                val u = Uri.parse(uri.toString())
                                                viewModel.removeUri(u)
                                            },
                                            modifier = Modifier.align(Alignment.End)
                                        ) {
                                            //Text(stringResource(R.string.delete))
                                            Image(
                                                modifier = Modifier
                                                    .size(25.dp)
                                                    .padding(2.dp),
                                                painter = painterResource(R.drawable.eliminar),
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


