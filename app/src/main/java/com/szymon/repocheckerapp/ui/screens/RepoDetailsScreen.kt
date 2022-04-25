package com.szymon.repocheckerapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RepoDetailsScreen(
    userName: String,
    repoName: String
) {

    Column() {
        Text(text = userName);
        Text(text = repoName);
    }

}