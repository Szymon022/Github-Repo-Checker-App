package com.szymon.repocheckerapp.ui.screens

sealed class Screen(val route: String) {
    object RepoListScreen : Screen("repoListScreen")
    object RepoDetailsScreen : Screen("repoDetailsScreen") {
        // function for passing arguments to RepoDetailsScreen
        fun withArgs(userName : String, repoName : String) : String {
            return buildString {
                append(route)
                append("/$userName")
                append("/$repoName")
            }
        }
    }


}
