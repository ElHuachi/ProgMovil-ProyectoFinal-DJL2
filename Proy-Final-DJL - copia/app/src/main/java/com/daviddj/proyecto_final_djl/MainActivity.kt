package com.daviddj.proyecto_final_djl

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.daviddj.proyecto_final_djl.model.Multimedia
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
                    NavHost(navController = navigationcontroller, startDestination = Routes.NotasScreen.route){
                        composable(Routes.NotasScreen.route){
                            NotasList(navController = navigationcontroller, windowSize = windowSize.widthSizeClass, navigateToItemUpdate = {
                                navigationcontroller.navigate("${NotasDetallesDestination.route}/${it}")
                            })
                        }
                        composable(Routes.TareasScreen.route){
                            TareasList(navController = navigationcontroller, navigateToItemUpdate = {
                                navigationcontroller.navigate("${TareasDetallesDestination.route}/${it}")
                            })
                        }
                        composable(
                            route = NotasDetallesDestination.routeWithArgs,
                            arguments = listOf(navArgument(NotasDetallesDestination.notaIdArg) {
                                type = NavType.IntType
                            })
                        ) {
                            NotaDetailsScreen(
                                navigateToEditItem = { navigationcontroller.navigate("${NotaEditDestination.route}/$it") },
                                navigateBack = { navigationcontroller.navigateUp() }
                            )
                        }
                        composable(
                            route = TareasDetallesDestination.routeWithArgs,
                            arguments = listOf(navArgument(TareasDetallesDestination.tareaIdArg) {
                                type = NavType.IntType
                            })
                        ) {
                            TareaDetailsScreen(
                                navigateToEditItem = { navigationcontroller.navigate("${TareaEditDestination.route}/$it") },
                                navigateBack = { navigationcontroller.navigateUp() }
                            )
                        }
                        composable(
                            route = TareaEditDestination.routeWithArgs,
                            arguments = listOf(navArgument(TareaEditDestination.itemIdArg) {
                                type = NavType.IntType
                            })
                        ) {
                            UpdateTareaScreen(
                                navigateBack = { navigationcontroller.popBackStack() },
                                onNavigateUp = { navigationcontroller.navigateUp() }
                            )
                        }
                        composable(
                            route = NotaEditDestination.routeWithArgs,
                            arguments = listOf(navArgument(NotaEditDestination.itemIdArg) {
                                type = NavType.IntType
                            })
                        ) {
                            UpdateNotaScreen(
                                navigateBack = { navigationcontroller.popBackStack() },
                                onNavigateUp = { navigationcontroller.navigateUp() }
                            )
                        }
                        composable(Routes.NotasEditor.route){
                            EditorNotas(navigateBack = { navigationcontroller.popBackStack()}, navController = navigationcontroller)
                        }
                        composable(Routes.TareasEditor.route){
                            EditorTareas(navigateBack = { navigationcontroller.popBackStack()}, navController = navigationcontroller)
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

//    IconButton(
//        onClick = {
//            if (isPlaying) {
//                exoPlayer.pause()
//            } else {
//                exoPlayer.play()
//            }
//        },
//        modifier = Modifier
//            //.align(Alignment.BottomEnd)
//            .padding(16.dp)
//    ) {
//        Icon(
//            imageVector = if (isPlaying) Icons.Filled.Refresh else Icons.Filled.PlayArrow,
//            contentDescription = if (isPlaying) "Pause" else "Play",
//            tint = Color.White,
//            modifier = Modifier.size(48.dp)
//        )
//    }
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

//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    ProyectoFinalDJLTheme {
//        Surface {
//            NotasList(navController = rememberNavController(), windowSize = WindowWidthSizeClass.Medium)
//        }
//    }
//}

//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true, widthDp = 700)
//@Composable
//fun GreetingPreview() {
//    ProyectoFinalDJLTheme(useDarkTheme=false) {
//        Surface {
//            NotasList(navController = rememberNavController(), windowSize = WindowWidthSizeClass.Medium)
//        }
//    }
//}

//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true, widthDp = 1000)
//@Composable
//fun GreetingExpandedPreview() {
//    ProyectoFinalDJLTheme(useDarkTheme=false) {
//        Surface {
//            NotasList(navController = rememberNavController(), windowSize = WindowWidthSizeClass.Expanded)
//        }
//    }
//}