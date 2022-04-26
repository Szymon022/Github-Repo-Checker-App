package com.szymon.repocheckerapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.szymon.repocheckerapp.data.GithubRepo
import com.szymon.repocheckerapp.remote.GithubApi
import com.szymon.repocheckerapp.remote.GithubService
import com.szymon.repocheckerapp.remote.responses.GithubReposListItem
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun RepoListScreen(
    navController: NavController
) {
    val repoList = remember{ ArrayList<GithubRepo>().toMutableStateList() }
    val githubService = GithubService()

    Surface(
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            SearchBar(hint = "Search github user..." ,onSearch = {
                if(it != "") {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val response = githubService.getUserRepos(it)

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
                else {
                    if(repoList.size > 0) {
                        repoList.removeRange(0, repoList.size)
                    }
                }
            }
            )
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
            RepoEntry(
                navController = navController,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                entry = it)
        }
    }
}

@Composable
fun RepoEntry(
    modifier: Modifier = Modifier,
    navController: NavController,
    entry : GithubRepo
) {

    Surface(
        color = MaterialTheme.colors.primary,
        modifier = modifier
            .clickable {
                navController.navigate("repoDetailsScreen/${entry.owner}/${entry.repoName}")
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


@Composable
fun SearchBar(
    hint : String = "",
    onSearch: (String) -> Unit = {}
) {
    var text by rememberSaveable {
        mutableStateOf("")
    }
    var isHintDisplayed by rememberSaveable {
        mutableStateOf(hint != "")
    }

    var searchPerformed by rememberSaveable {
        mutableStateOf(false)
    }

    if(searchPerformed) {
        onSearch(text)
    }

    Box(
//        modifier = modifier
    ) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                isHintDisplayed = text == ""
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if(isHintDisplayed) hint else "",
                color = Color.LightGray,
                modifier = Modifier
                    .padding(vertical = 12.dp, horizontal = 20.dp)

            )
            IconButton(
                modifier = Modifier.padding( horizontal = 20.dp),
                onClick = {
                    searchPerformed = true
                    onSearch(text)
                }
            ) {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Search for user on Github",
                    tint = MaterialTheme.colors.primary
                )
            }
        }
    }
}