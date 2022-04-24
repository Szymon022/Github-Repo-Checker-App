package com.szymon.repocheckerapp.remote

import com.szymon.repocheckerapp.remote.responses.GithubReposList
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubApi {

    @GET("users/{userName}/repos")
    suspend fun getUserRepos(
        @Path("userName") userName : String,
    ) : GithubReposList

}