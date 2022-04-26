package com.szymon.repocheckerapp.ui.screens


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.szymon.repocheckerapp.data.GithubRepo
import com.szymon.repocheckerapp.remote.GithubService
import kotlinx.coroutines.*

@Composable
fun RepoListScreen(
    navController: NavController
) {
    val repoList = remember{ ArrayList<GithubRepo>().toMutableStateList() }
    var userName by rememberSaveable { mutableStateOf("") }
    val githubService = GithubService()
    var searchPerformed by rememberSaveable { mutableStateOf(false) }

    // it will execute every time we go back to the repoListScreen
    if(searchPerformed) {
        LaunchedEffect(Dispatchers.IO) {
            try {
                val response = githubService.getUserRepos(userName)

                if(repoList.size > 0) {
                    repoList.clear()
                }

                response.forEach { githubRepo ->
                    repoList.add(
                        GithubRepo(
                            repoName = githubRepo.name,
                            owner = githubRepo.owner.login,
                            languagesUrl = githubRepo.languages_url
                        )
                    )
                }
            } catch(e : Exception) {

            }
        }
    }

    Surface(
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            OutlinedTextField(
                label = { Text(text = "Github User") },
                value = userName,
                onValueChange = { userName = it })
            Button(
                onClick = {  // search GithubUser
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val response = githubService.getUserRepos(userName)

                            if(repoList.size > 0) {
                                repoList.clear()
                            }

                            response.forEach { githubRepo ->
                                repoList.add(
                                    GithubRepo(
                                        repoName = githubRepo.name,
                                        owner = githubRepo.owner.login,
                                        languagesUrl = githubRepo.languages_url
                                    )
                                )
                            }
                        } catch(e : Exception) {

                        }
                    }
                    searchPerformed = true
                }
            ) {
                Text(text = "Search")
            }
            Text(
                text = "List of repositories:",
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 4.dp, top = 8.dp)
            )
            RepoList(
                entries = repoList,
                navController = navController,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 8.dp)
            )
        }
    }

}

@Composable
fun RepoList(
    modifier: Modifier = Modifier,
    navController: NavController,
    entries: SnapshotStateList<GithubRepo>
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            items = entries
        ) {
            RepoListEntry(
                navController = navController,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                entry = it)
        }
    }
}

@Composable
fun RepoListEntry(
    modifier: Modifier = Modifier,
    navController: NavController,
    entry : GithubRepo
) {

    Surface(
        color = MaterialTheme.colors.primary,
        modifier = modifier
            .clickable {
                navController.navigate(Screen.RepoDetailsScreen.withArgs(entry.owner, entry.repoName))
            }
    ) {
        Row(
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
                text = entry.repoName,
                fontSize = 15.sp,
                color = MaterialTheme.colors.onPrimary
            )
        }
    }
}