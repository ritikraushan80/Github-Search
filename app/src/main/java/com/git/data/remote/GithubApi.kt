package com.git.data.remote

import com.git.data.model.Repo
import com.git.data.model.User
import com.git.data.model.UserSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * Created by Ritik on: 28/08/25
 */

interface GithubApi {

    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("per_page") perPage: Int = 30,
        @Query("page") page: Int = 1
    ): UserSearchResponse

    @GET("users/{username}")
    suspend fun getUser(
        @Path("username") username: String
    ): User

    @GET("users/{username}/repos")
    suspend fun getUserRepos(
        @Path("username") username: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): List<Repo>
}