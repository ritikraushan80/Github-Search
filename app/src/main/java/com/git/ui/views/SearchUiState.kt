package com.git.ui.views

import com.git.data.model.UserSearchItem


/**
 * Created by Ritik on: 28/08/25
 */

sealed class SearchUiState {
    object Idle : SearchUiState()
    object Loading : SearchUiState()
    data class Success(val users: List<UserSearchItem>) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}