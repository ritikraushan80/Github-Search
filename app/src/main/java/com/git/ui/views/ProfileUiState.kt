package com.git.ui.views

import com.git.data.model.User

/**
 * Created by Ritik on: 28/08/25
 */

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val user: User) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}