package com.git.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.git.data.model.Repo
import com.git.data.model.RepoPagingSource
import com.git.data.model.User
import com.git.data.model.UserSearchResponse
import com.git.data.remote.GithubApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


/**
 * Created by Ritik on: 28/08/25
 */

class UserRepository @Inject constructor(
    private val api: GithubApi
) {

    suspend fun searchUsers(query: String): UserSearchResponse {
        return api.searchUsers(query)
    }

    suspend fun getUser(username: String): User {
        return api.getUser(username)
    }

    fun getUserRepos(username: String): Flow<PagingData<Repo>> {
        return Pager(
            config = PagingConfig(pageSize = 30, enablePlaceholders = false),
            pagingSourceFactory = { RepoPagingSource(api, username) }).flow
    }
}