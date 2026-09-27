package com.ifsp.crossfitwod.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ifsp.crossfitwod.ui.home.HomeScreen
import com.ifsp.crossfitwod.ui.result.ResultScreen
import com.ifsp.crossfitwod.ui.wod.WodDetailScreen
import com.ifsp.crossfitwod.ui.wod.WodFormScreen
import com.ifsp.crossfitwod.viewmodel.WodViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object WodForm : Screen("wod_form") {
        const val routeWithArgs = "wod_form?wodId={wodId}"
        const val argWodId = "wodId"
    }
    object WodDetail : Screen("wod_detail/{wodId}") {
        const val argWodId = "wodId"
    }
    object Result : Screen("result/{wodId}") {
        const val argWodId = "wodId"
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: WodViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onAddWod = { navController.navigate(Screen.WodForm.route) },
                onWodClick = { wodId -> navController.navigate("wod_detail/$wodId") }
            )
        }

        composable(
            route = Screen.WodForm.routeWithArgs,
            arguments = listOf(
                navArgument(Screen.WodForm.argWodId) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val wodId = backStackEntry.arguments?.getString(Screen.WodForm.argWodId)
            WodFormScreen(
                viewModel = viewModel,
                wodId = wodId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.WodDetail.route,
            arguments = listOf(
                navArgument(Screen.WodDetail.argWodId) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val wodId = backStackEntry.arguments?.getString(Screen.WodDetail.argWodId) ?: return@composable
            WodDetailScreen(
                viewModel = viewModel,
                wodId = wodId,
                onNavigateBack = { navController.popBackStack() },
                onEditWod = { id -> navController.navigate("wod_form?wodId=$id") },
                onRegisterResult = { id -> navController.navigate("result/$id") }
            )
        }

        composable(
            route = Screen.Result.route,
            arguments = listOf(
                navArgument(Screen.Result.argWodId) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val wodId = backStackEntry.arguments?.getString(Screen.Result.argWodId) ?: return@composable
            ResultScreen(
                viewModel = viewModel,
                wodId = wodId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
