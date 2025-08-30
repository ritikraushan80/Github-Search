package com.git.data.model

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.git.data.remote.GithubApi

/**
 * Created by Ritik on: 28/08/25
 */

class RepoPagingSource(
    private val api: GithubApi, private val username: String
) : PagingSource<Int, Repo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        return try {
            val page = params.key ?: 1
            val repos = api.getUserRepos(username, params.loadSize, page)
            LoadResult.Page(
                data = repos,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (repos.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            Log.e("RepoPagingSource", "Error loading repos: ${e.message}")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}