package com.git.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Ritik on: 28/08/25
 */

data class UserSearchResponse(
    @SerializedName("items") val items: List<UserSearchItem> = listOf(),
)

data class UserSearchItem(
    @SerializedName("login") val login: String,
    @SerializedName("avatar_url") val avatarUrl: String,
)

data class User(
    @SerializedName("login") val login: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("bio") val bio: String,
    @SerializedName("followers") val followers: Int,
    @SerializedName("public_repos") val publicRepos: Int,
)

data class Repo(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("stargazers_count") val stargazersCount: Int,
    @SerializedName("forks_count") val forksCount: Int
)