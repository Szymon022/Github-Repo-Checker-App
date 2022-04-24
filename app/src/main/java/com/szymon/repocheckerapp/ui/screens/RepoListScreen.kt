package com.szymon.repocheckerapp.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.szymon.repocheckerapp.remote.GithubApi
import com.szymon.repocheckerapp.remote.responses.GithubReposListItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Preview(showBackground = true)
@Composable
fun RepoListScreen() {
    val repoList = remember { ArrayList<GithubReposListItem>().toMutableStateList() }

    val githubService = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GithubApi::class.java)

    Surface(
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            SearchBar(hint = "Search github user...", onSearch = {
                if(it != "") {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val response = githubService.getUserRepos(it)
                            if(repoList.size > 0) {
                                repoList.removeRange(0, repoList.size)
                            }
                            response.forEach { GithubReposListItem ->
                                repoList.add(GithubReposListItem)
                            }
                        } catch(e : Exception)
                        {

                        }
                    }
                }
                else
                {
                    if(repoList.size > 0) {
                        repoList.removeRange(0, repoList.size)
                    }
                }
            })
            Text(
                text = "List of repositories:",
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 4.dp, top = 8.dp)
            )

            RepoList(
                entries = repoList,
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
    entries: SnapshotStateList<GithubReposListItem>
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            items = entries
        ) {
            RepoEntry(
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
    entry : GithubReposListItem
) {
    Surface(
        color = MaterialTheme.colors.primary,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = entry.name,
                fontSize = 20.sp,
            )
        }

    }
}


@Composable
fun SearchBar(
    hint : String = "",
    onSearch: (String) -> Unit = {}
) {
    var text by remember {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
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