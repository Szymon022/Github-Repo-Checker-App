package com.szymon.repocheckerapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.szymon.repocheckerapp.remote.GithubService
import kotlinx.coroutines.Dispatchers


@Composable
fun RepoDetailsScreen(
    userName : String,
    repoName : String
) {
    Surface {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            var validate by remember { mutableStateOf(0) }
            var languagesMap = mapOf<String, Int>()
            val githubService = GithubService()

            Text(modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
                text = "List of used languages and bytes of code"
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                // force recomposition of items after languagesMap was acquired

                validate.let {
                  items(items = languagesMap.toList()) {
                      LanguageListEntry(language = it.first, bytesUsed = it.second.toLong())
                  }
                }
            }
            LaunchedEffect(Dispatchers.IO) {
                try {
                    languagesMap = githubService.getRepoLanguageDetails(userName, repoName)
                    validate++
                } catch (e: Exception) {

                }
            }
        }
    }
}

@Composable
fun LanguageListEntry(
    language: String,
    bytesUsed: Long
) {
    Row(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .background(color = MaterialTheme.colors.primary)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
            text = language,
            color = MaterialTheme.colors.onPrimary,
            fontSize = 15.sp
        )
        Text(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
            text = "$bytesUsed",
            color = MaterialTheme.colors.onPrimary,
            fontSize = 15.sp
        )
    }

}