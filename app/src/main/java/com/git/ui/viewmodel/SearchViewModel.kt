package com.git.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.git.data.repository.UserRepository
import com.git.ui.views.SearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by Ritik on: 29/08/25
 */


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState

    val queryState: MutableStateFlow<String> = MutableStateFlow("")

    init {
        observeQuery()
    }

    /**------------------ Search Users ---------------------*/
    fun searchUsers(query: String) {
        if (query.isBlank()) {
            _uiState.value = SearchUiState.Idle
            return
        }
        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading
            try {
                val response = repository.searchUsers(query)
                if (response.items.isEmpty()) {
                    _uiState.value = SearchUiState.Error("User not found")
                } else {
                    _uiState.value = SearchUiState.Success(response.items)
                }
            } catch (e: Exception) {
                _uiState.value = SearchUiState.Error("Network error: ${e.message}")
            }
        }
    }

    fun onQueryChanged(newQuery: String) {
        queryState.update { newQuery }
    }

    @OptIn(FlowPreview::class)
    private fun observeQuery() {
        viewModelScope.launch {
            queryState
                .debounce(400)
                .map { it.trim() }
                .distinctUntilChanged()
                .collect { q ->
                    if (q.isBlank()) {
                        _uiState.value = SearchUiState.Idle
                    } else {
                        searchUsers(q)
                    }
                }
        }
    }
}