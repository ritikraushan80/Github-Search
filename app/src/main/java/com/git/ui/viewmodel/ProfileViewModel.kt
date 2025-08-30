package com.git.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.git.data.model.Repo
import com.git.data.repository.UserRepository
import com.git.ui.views.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by Ritik on: 29/08/25
 */

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState

    private var _repos: Flow<PagingData<Repo>>? = null
    fun repos(username: String): Flow<PagingData<Repo>> {
        if (_repos == null) {
            _repos = repository.getUserRepos(username).cachedIn(viewModelScope)
        }
        return _repos!!
    }

    fun loadProfile(username: String) {
        viewModelScope.launch {
            try {
                val user = repository.getUser(username)
                _uiState.value = ProfileUiState.Success(user)
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error("Error loading profile: ${e.message}")
            }
        }
    }
}
