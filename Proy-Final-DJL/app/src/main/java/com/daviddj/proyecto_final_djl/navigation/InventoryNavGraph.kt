package com.daviddj.proyecto_final_djl.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.daviddj.proyecto_final_djl.tareasDetails.ItemEntryScreen
import com.daviddj.proyecto_final_djl.tareasDetails.TareaDetailsDestination
import com.daviddj.proyecto_final_djl.tareasDetails.TareaEditDestination
import com.daviddj.proyecto_final_djl.tareasDetails.TareaEntryDestination
import com.daviddj.proyecto_final_djl.notaDetails.ItemDetailsDestination
import com.daviddj.proyecto_final_djl.notaDetails.ItemDetailsScreen
import com.daviddj.proyecto_final_djl.notaDetails.ItemEditDestination
import com.daviddj.proyecto_final_djl.notaDetails.ItemEditScreen
import com.daviddj.proyecto_final_djl.model.NotasInfo
import com.daviddj.proyecto_final_djl.model.TareasInfo
import com.daviddj.proyecto_final_djl.tareasDetails.TareaDetailsScreen
import com.daviddj.proyecto_final_djl.tareasDetails.TareaEditScreen
import com.daviddj.proyecto_final_djl.ui.EditorNotas
import com.daviddj.proyecto_final_djl.ui.EditorTareas
import com.daviddj.proyecto_final_djl.ui.NotasList
import com.daviddj.proyecto_final_djl.ui.Routes
import com.daviddj.proyecto_final_djl.ui.TareasList
import com.daviddj.proyecto_final_djl.viewModel.TareaDetails


/**
 * Provides Navigation graph for the application.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InventoryNavHost(
    navController: NavHostController,
    windowSize : WindowSizeClass,
    modifier: Modifier = Modifier,
) {
    NavHost(navController = navController,
        startDestination = Routes.NotasScreen.route){
        composable(Routes.NotasScreen.route){
            NotasList(notas = NotasInfo.notas,
                navController = navController, windowSize = windowSize.widthSizeClass,
                navigateToItemUpdate = {
                    navController.navigate(
                        "${ItemDetailsDestination.route}/${it}")
                }
            )
            /*
            navigateToItemUpdate = {
                    navController.navigate(
                        "${ItemDetailsDestination.route}/${it}")
                }
             */
        }
        composable(Routes.NotasEditor.route){
            EditorNotas(navigateBack = { navController.popBackStack()},
                navController = navController)
        }

        composable(Routes.TareasEditor.route){
            EditorTareas(navigateBack = { navController.popBackStack()},
                navController = navController, TareaDetails = TareaDetails())
        }


        //Esta es la pequeña ventana que sale antes del esdit, es la preview de la nota
        composable(
            route = ItemDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemDetailsDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ItemDetailsScreen(
                navigateToEditItem = { navController.navigate("${ItemEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }

        composable(
            route = ItemEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemEditDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ItemEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        //}}}}}}}}}}}}}}}}}}}}}}}}}}

        composable(Routes.TareasScreen.route){
            TareasList(tareas = TareasInfo.tareas,
                navController = navController, windowSize = windowSize.widthSizeClass,
                navigateToItemUpdate = {
                    navController.navigate(
                        "${TareaDetailsDestination.route}/${it}")
                }
            )
            /*
            navigateToItemUpdate = {
                    navController.navigate(
                        "${ItemDetailsDestination.route}/${it}")
                }
             */
        }

        composable(route = TareaEntryDestination.route) {
            ItemEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = TareaDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(TareaDetailsDestination.tareaIdArg) {
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
            TareaEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}
