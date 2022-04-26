package com.szymon.repocheckerapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun App() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.RepoListScreen.route) {
        composable(Screen.RepoListScreen.route) {
            RepoListScreen(navController)
        }

        composable(
            Screen.RepoDetailsScreen.route + "/{userName}/{repoName}",
            arguments = listOf(
                navArgument("userName") {
                    type = NavType.StringType
                },
                navArgument("repoName") {
                    type = NavType.StringType
                },
            )
        ) {
            RepoDetailsScreen(
                userName = it.arguments?.getString("userName")!!,
                repoName = it.arguments?.getString("repoName")!!
            )
        }
    }

}