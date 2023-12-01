package com.daviddj.proyecto_final_djl

import android.app.Activity
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.daviddj.proyecto_final_djl.ui.AlarmSchedulerImpl
import com.daviddj.proyecto_final_djl.ui.AndroidAudioPlayer
import com.daviddj.proyecto_final_djl.ui.AndroidAudioRecorder
import com.daviddj.proyecto_final_djl.ui.EditorNotas
import com.daviddj.proyecto_final_djl.ui.EditorTareas
import com.daviddj.proyecto_final_djl.ui.NotaDetailsScreen
import com.daviddj.proyecto_final_djl.ui.NotaEditDestination
import com.daviddj.proyecto_final_djl.ui.NotasDetallesDestination
import com.daviddj.proyecto_final_djl.ui.NotasList
import com.daviddj.proyecto_final_djl.ui.Routes
import com.daviddj.proyecto_final_djl.ui.TareaDetailsScreen
import com.daviddj.proyecto_final_djl.ui.TareaEditDestination
import com.daviddj.proyecto_final_djl.ui.TareasDetallesDestination
import com.daviddj.proyecto_final_djl.ui.TareasList
import com.daviddj.proyecto_final_djl.ui.UpdateNotaScreen
import com.daviddj.proyecto_final_djl.ui.UpdateTareaScreen
import com.daviddj.proyecto_final_djl.ui.theme.ProyectoFinalDJLTheme
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import java.io.File

class MainActivity : ComponentActivity() {
    private val recorder by lazy {
        AndroidAudioRecorder(applicationContext)
    }

    private val player by lazy {
        AndroidAudioPlayer(applicationContext)
    }

    private var audioFile: File? = null

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainActivityScreen()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
    @Composable
    fun MainActivityScreen() {
        val context = LocalContext.current
        val windowSize = calculateWindowSizeClass(context as Activity)
        val navController = rememberNavController()

        ProyectoFinalDJLTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Routes.NotasScreen.route
                ) {
                    composable(Routes.NotasScreen.route) {
                        NotasList(
                            navController = navController,
                            configuration = LocalConfiguration.current,
                            navigateToItemUpdate = {
                                navController.navigate("${NotasDetallesDestination.route}/${it}")
                            }
                        )
                    }
                    composable(Routes.TareasScreen.route) {
                        TareasList(
                            navController = navController,
                            configuration = LocalConfiguration.current,
                            navigateToItemUpdate = {
                            navController.navigate("${TareasDetallesDestination.route}/${it}")
                        })
                    }
                    composable(
                        route = NotasDetallesDestination.routeWithArgs,
                        arguments = listOf(navArgument(NotasDetallesDestination.notaIdArg) {
                            type = NavType.IntType
                        })
                    ) {
                        NotaDetailsScreen(
                            navigateToEditItem = { navController.navigate("${NotaEditDestination.route}/$it") },
                            navigateBack = { navController.navigateUp() }
                        )
                    }
                    composable(
                        route = TareasDetallesDestination.routeWithArgs,
                        arguments = listOf(navArgument(TareasDetallesDestination.tareaIdArg) {
                            type = NavType.IntType
                        })
                    ) {
                        TareaDetailsScreen(
                            navigateToEditItem = { navController.navigate("${TareaEditDestination.route}/$it") },
                            navigateBack = { navController.navigateUp() }
                        )
                    }
                    composable(
                        route = TareaEditDestination.routeWithArgs,
                        arguments = listOf(navArgument(TareaEditDestination.itemIdArg) {
                            type = NavType.IntType
                        })
                    ) {
                        UpdateTareaScreen(
                            navigateBack = { navController.popBackStack() },
                            onNavigateUp = { navController.navigateUp() },
                            alarmScheduler = AlarmSchedulerImpl(context)
                        )
                    }
                    composable(
                        route = NotaEditDestination.routeWithArgs,
                        arguments = listOf(navArgument(NotaEditDestination.itemIdArg) {
                            type = NavType.IntType
                        })
                    ) {
                        UpdateNotaScreen(
                            navigateBack = { navController.popBackStack() },
                            onNavigateUp = { navController.navigateUp() }
                        )
                    }
                    composable(Routes.NotasEditor.route) {
                        EditorNotas(
                            navigateBack = { navController.popBackStack() },
                            navController = navController,
                            onClickStGra = {
                                File(context.cacheDir, "audio.mp3").also {
                                    recorder.start(it)
                                    audioFile = it
                                }
                            },
                            onClickSpGra = { recorder.stop() },
                            onClickStRe = { audioFile?.let { player.start(it) } },
                            onClickSpRe = { player.stop() })
                    }
                    composable(Routes.TareasEditor.route) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            EditorTareas(
                                navigateBack = { navController.popBackStack() },
                                navController = navController,
                                alarmScheduler = AlarmSchedulerImpl(context)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(title: String, ) {
    TopAppBar(
        title = { Text(text = title, style = TextStyle(fontWeight = FontWeight.Bold, fontSize= 30.sp)) },
    )
}

@Composable
fun VideoPlayer(videoUri: Uri, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val exoPlayer = remember {
        SimpleExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUri))
            prepare()
        }
    }
    val playbackState = exoPlayer
    val isPlaying = playbackState?.isPlaying ?: false

    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                player = exoPlayer
            }
        },
        modifier = modifier
    )
}

//BOTONES DE NAVEGACION DE LA PARTE SUPERIOR
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(modifier: Modifier = Modifier, navController: NavController) {
    val orientation = LocalContext.current.resources.configuration.orientation
    val buttonSize = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        50.dp // Tamaño más grande para landscape
    } else {
        40.dp // Tamaño normal para portrait
    }
    CenterAlignedTopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), // Añadir espacio entre los bordes de la pantalla y los botones
                horizontalArrangement = Arrangement.SpaceBetween // Espaciado entre los botones
            ) {
                Button(
                    onClick = { navController.navigate(Routes.NotasScreen.route) },
                    modifier = Modifier
                        .weight(1f) // El botón se expandirá para ocupar todo el espacio disponible
                        .padding(end = 8.dp) // Añadir espacio entre los botones
                ) {
                    Image(
                        modifier = Modifier
                            .size(buttonSize)
                            .padding(4.dp),
                        painter = painterResource(R.drawable.notas),
                        contentDescription = null
                    )
                    Text(
                        text = stringResource(R.string.notas),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Button(
                    onClick = { navController.navigate(Routes.TareasScreen.route) },
                    modifier = Modifier
                        .weight(1f) // El botón se expandirá para ocupar todo el espacio disponible
                        .padding(start = 8.dp) // Añadir espacio entre los botones
                ) {
                    Image(
                        modifier = Modifier
                            .size(buttonSize)
                            .padding(4.dp),
                        painter = painterResource(R.drawable.tareas),
                        contentDescription = null
                    )
                    Text(
                        text = stringResource(R.string.tareas),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        },
        modifier = modifier
    )
}

//BARRA DE BUSQUEDA
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
fun InventoryTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}