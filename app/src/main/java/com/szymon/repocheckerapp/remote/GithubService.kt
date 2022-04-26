package com.szymon.repocheckerapp.remote

import com.szymon.repocheckerapp.remote.responses.GithubReposList
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GithubService() {
    private var githubService : GithubApi = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GithubApi::class.java)

    suspend fun getUserRepos(
        userName : String
    ) = githubService.getUserRepos(userName)

    suspend fun getRepoLanguageDetails(
        userName: String,
        repoName: String
    ) = githubService.getRepoLanguagesDetails(userName, repoName)
}