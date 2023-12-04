package com.daviddj.proyecto_final_djl.ui

import android.Manifest
import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.daviddj.proyecto_final_djl.AppViewModelProvider
import com.daviddj.proyecto_final_djl.ComposeFileProvider
import com.daviddj.proyecto_final_djl.R
import com.daviddj.proyecto_final_djl.VideoPlayer
import com.daviddj.proyecto_final_djl.viewModel.NotaDetails
import com.daviddj.proyecto_final_djl.viewModel.NotaUiState
import com.daviddj.proyecto_final_djl.viewModel.NotasEditorViewModel
import com.daviddj.proyecto_final_djl.viewModel.UpdateNotaViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

var audioUri: Uri? = null

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditorNotas(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: NotasEditorViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onClickStGra: () -> Unit,
    onClickSpGra: () -> Unit,
    onClickStRe: () -> Unit,
    onClickSpRe: () -> Unit,
){
    val coroutineScope = rememberCoroutineScope()
    var imageUris by remember { mutableStateOf(listOf<Uri>()) }
    var videoUris by remember { mutableStateOf(listOf<Uri>()) }
    var audioUris by remember { mutableStateOf(listOf<Uri>()) }
    var audioFile: File? = null


    val context = LocalContext.current
    val audioRecorder = AndroidAudioRecorder(context)

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

    //MULTIMEDIA

    //AUDIO
    val recordAudioPermissionState = rememberPermissionState(
        Manifest.permission.RECORD_AUDIO
    )

    //Realiza un seguimiento del estado del diálogo de justificación, necesario cuando el usuario requiere más justificación
    var rationaleState by remember {
        mutableStateOf<RationaleState?>(null)
    }
    //AUDIO

    Column (
        modifier= Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row (horizontalArrangement = Arrangement.Start) {
            IconButton(onClick = {
                navController.navigate(Routes.NotasScreen.route) }
            ){
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Regresar",
                    modifier = Modifier
                        .size(32.dp)
                        .padding(0.dp)
                )
            }
        }
        NotaEntryBody(
            notaUiState = viewModel.notaUiState,
            onNotaValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateUiState(viewModel.notaUiState.notaDetails)
                    viewModel.saveNota()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(5.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
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
        }
        Spacer(modifier = Modifier.height(16.dp))//MOSTRAR MULTIMEDIA
        Row{
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                //.animateContentSize(),
                //verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Show rationale dialog when needed
                rationaleState?.run { PermissionRationaleDialog(rationaleState = this) }

                PermissionRequestButton(
                    isGranted = recordAudioPermissionState.status.isGranted,
                    title = stringResource(R.string.record_audio),
                    onClickStGra,
                    onClickSpGra,
                    viewModel.audioUris,
                    viewModel
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
        Spacer(modifier = Modifier.height(16.dp))//MOSTRAR MULTIMEDIA
        Box {
            LazyColumn(modifier = Modifier.align(Alignment.Center)) {
                items(imageUris + videoUris + viewModel.audioUris) { uri ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .zIndex(0f)
                    ) {
                        Column {
                            if (uri in imageUris) {
                                AsyncImage(
                                    model = uri,
                                    modifier = Modifier
                                        .height(400.dp)
                                        .fillMaxWidth(),
                                    contentDescription = "Selected image",
                                )
                            } else if (uri in videoUris) {
                                VideoPlayer(
                                    videoUri = uri,
                                    modifier = Modifier
                                        .height(400.dp)
                                        .fillMaxWidth()
                                )
                            } else if (uri in viewModel.audioUris) {
                                val audioPlayer2 = AndroidAudioPlayer2(context,uri)
                                PermissionRequestButton2(
                                    isGranted = recordAudioPermissionState.status.isGranted,
                                    title = stringResource(R.string.record_audio),
                                    onClickSpGra,
                                    onClickStRe,
                                    onClickStRe = {
                                        audioPlayer2.start(uri)
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
                            // Obtén la descripción actual del ViewModel
//                            TextField(
//                                value = viewModel.notaMultimediaUiState.notaMultimediaDetails.descripcion,
//                                onValueChange = { newDescription ->
//                                    viewModel.setNotaMultimediaUiState(
//                                        viewModel.notaMultimediaUiState.copy(
//                                            notaMultimediaDetails = viewModel.notaMultimediaUiState.notaMultimediaDetails.copy(descripcion = newDescription)
//                                        )
//                                    )
//                                },
//                                label = { Text("Descripción") },
//                                modifier = Modifier.fillMaxWidth(),
//                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
//                            )
                            Button(
                                onClick = {
                                    // Elimina la tarjeta y quita la imagen del arreglo.
                                    imageUris = imageUris.filter { it != uri }
                                    videoUris = videoUris.filter { it != uri }
                                    viewModel.removeUri(uri)
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
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
        //Spacer(modifier = Modifier.height(16.dp))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PermissionRequestButton(
    isGranted: Boolean, title: String,
    onClickStGra: () -> Unit,
    onClickSpGra: () -> Unit,
    audioUris: List<Uri>,
    notasEditorViewModel: NotasEditorViewModel,
    onClick: () -> Unit,
) {
    if (isGranted) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
        ){
            Button(onClick = {
                onClickStGra()
            }) {
                //Text("Grabar")
                Image(
                    modifier = Modifier
                        .size(25.dp)
                        .padding(2.dp),
                    painter = painterResource(R.drawable.microfono_grabador),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Button(onClick = {
                onClickSpGra()
                audioUris.plus(audioUri!!)
                notasEditorViewModel.audioUris = notasEditorViewModel.audioUris.plus(audioUri!!)
                Log.e("URI",audioUri.toString())
            }) {
                Image(
                    modifier = Modifier
                        .size(25.dp)
                        .padding(2.dp),
                    painter = painterResource(R.drawable.cuadra),
                    contentDescription = null
                )
            }
        }
    } else {
        Button(onClick = onClick) {
            Text("Request $title")
        }
    }
}

@Composable
fun PermissionRequestButton2(
    isGranted: Boolean, title: String,
    onClickStGra: () -> Unit,
    onClickSpGra: () -> Unit,
    onClickStRe: () -> Unit,
    onClickSpRe: () -> Unit,
    audioUris: List<Uri>,
    onClick: () -> Unit
) {
    if (isGranted) {
        //Spacer(modifier = Modifier.height(4.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
        ){
            Button(onClick = onClickStRe) {
                Image(
                    modifier = Modifier
                        .size(25.dp)
                        .padding(2.dp),
                    painter = painterResource(R.drawable.boton_de_play),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Button(onClick = onClickSpRe) {
                Image(
                    modifier = Modifier
                        .size(25.dp)
                        .padding(2.dp),
                    painter = painterResource(R.drawable.boton_detener),
                    contentDescription = null
                )
            }
        }
    } else {
        Button(onClick = onClick) {
            Text("Request $title")
        }
    }
}



/**
 * Simple AlertDialog that displays the given rational state
 * Cuadro de dialogo simple que muestra el estado del rational
 */
@Composable
fun PermissionRationaleDialog(rationaleState: RationaleState) {
    AlertDialog(onDismissRequest = { rationaleState.onRationaleReply(false) }, title = {
        Text(text = rationaleState.title)
    }, text = {
        Text(text = rationaleState.rationale)
    }, confirmButton = {
        TextButton(onClick = {
            rationaleState.onRationaleReply(true)
        }) {
            Text("Continue")
        }
    }, dismissButton = {
        TextButton(onClick = {
            rationaleState.onRationaleReply(false)
        }) {
            Text("Dismiss")
        }
    })
}
data class RationaleState(
    val title: String,
    val rationale: String,
    val onRationaleReply: (proceed: Boolean) -> Unit,
)

interface AudioRecorder {
    fun start(outputFile: File): Uri?
    fun stop()
}

interface AudioRecorder2 {
    fun start(audioUri: Uri): Uri?
    fun stop()
}

class AndroidAudioRecorder(
    private val context: Context,
    //private val viewModel: NotasEditorViewModel
): AudioRecorder {

    private var recorder: MediaRecorder? = null

    private fun createRecorder(): MediaRecorder {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else MediaRecorder()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun start(outputFile: File): Uri? {
        //this.outputFile = outputFile
        createRecorder().apply {
            Log.e("Uri",outputFile.toUri().toString())
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(FileOutputStream(outputFile).fd)

            prepare()
            start()

            recorder = this
        }
        audioUri=outputFile.toUri()
        Log.e("variable para guardar uri", audioUri.toString())
        return outputFile.toUri()
    }

    override fun stop() {
        recorder?.stop()
        recorder?.reset()
        recorder = null
    }
}

class AndroidAudioPlayer(
    private val context: Context
): AudioRecorder {

    private var player: MediaPlayer? = null

    override fun start(outputFile: File):Uri {
        MediaPlayer.create(context, outputFile.toUri()).apply {
            player = this
            start()
        }
        return outputFile.toUri()
    }

    override fun stop() {
        player?.stop()
        player?.release()
        player = null
    }
}

class AndroidAudioPlayer2(
    private val context: Context,
    private val audio: Uri
): AudioRecorder2 {

    private var player: MediaPlayer? = null

    override fun start(audioUri: Uri):Uri {
        MediaPlayer.create(context, audio).apply {
            player = this
            start()
        }
        return audio
    }

    override fun stop() {
        player?.stop()
        player?.release()
        player = null
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotaEntryBody(
    notaUiState: NotaUiState,
    onNotaValueChange: (NotaDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Column() {
        NotaInputForm(
            notaDetails = notaUiState.notaDetails,
            onValueChange = onNotaValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onSaveClick,
            enabled = notaUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_action))
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotaInputForm(
    notaDetails: NotaDetails,
    modifier: Modifier = Modifier,
    onValueChange: (NotaDetails) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
    ) {
        OutlinedTextField(
            value = notaDetails.name,
            onValueChange = { onValueChange(notaDetails.copy(name = it)) },
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
            value = notaDetails.contenido,
            onValueChange = { onValueChange(notaDetails.copy(contenido = it)) },
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

