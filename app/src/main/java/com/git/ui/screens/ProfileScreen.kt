package com.git.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.git.ui.component.NetworkErrorCard
import com.git.ui.component.ProfileHeaderCard
import com.git.ui.component.RepoItemCard
import com.git.ui.viewmodel.ProfileViewModel
import com.git.ui.views.ProfileUiState

/**
 * Created by Ritik on: 28/08/25
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    username: String,
    isDarkTheme: Boolean,
    onToggleTheme: (Boolean) -> Unit,
    onNavigateBack: () -> Unit
) {
    val viewModel: ProfileViewModel = hiltViewModel()
    LaunchedEffect(Unit) { viewModel.loadProfile(username) }
    val uiState by viewModel.uiState.collectAsState()
    val repos = viewModel.repos(username).collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ), navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }, actions = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = if (isDarkTheme) "Dark" else "Light")
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = isDarkTheme, onCheckedChange = { onToggleTheme(it) })
                }
            })
        }) { padding ->
        when (uiState) {
            is ProfileUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            is ProfileUiState.Success -> {
                val user = (uiState as ProfileUiState.Success).user

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    /**------------------ Profile Header ----------------*/
                    item {
                        ProfileHeaderCard(user = user)
                    }

                    /**---------------------  Repository List ------------------*/
                    items(repos.itemCount) { index ->
                        val repo = repos[index]
                        if (repo != null) {
                            RepoItemCard(repo = repo)
                        }
                    }

                    /**------------------- Loading More Repos ------------------*/
                    repos.apply {
                        when {
                            loadState.append is LoadState.Loading || loadState.refresh is LoadState.Loading -> {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            }

                            loadState.append is LoadState.Error -> {
                                item {
                                    Text(
                                        text = "Error loading more repos",
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            is ProfileUiState.Error -> Box(
                modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center
            ) {
                NetworkErrorCard(message = (uiState as ProfileUiState.Error).message)
            }
        }
    }
}