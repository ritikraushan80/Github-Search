package com.git.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.git.ui.component.NetworkErrorCard
import com.git.ui.component.UserCard
import com.git.ui.viewmodel.SearchViewModel
import com.git.ui.views.SearchUiState

/**
 * Created by Ritik on: 28/08/25
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController, isDarkTheme: Boolean, onToggleTheme: (Boolean) -> Unit
) {
    val viewModel: SearchViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val query by viewModel.queryState.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GitHub User Search") }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ), actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isDarkTheme) "Dark" else "Light",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = { onToggleTheme(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                checkedTrackColor = MaterialTheme.colorScheme.primary,
                                uncheckedThumbColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                    alpha = 0.7f
                                ),
                                uncheckedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                            )
                        )
                    }
                })
        }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            /**----------------------- Search Box -----------------------*/
            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.onQueryChanged(it) },
                label = { Text("Enter username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            /**--------------------- Search Button ---------------------*/
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = {
                    viewModel.searchUsers(query)
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }) {
                    Text("Search")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            /**----------------------- User List -----------------------*/
            when (uiState) {
                is SearchUiState.Loading -> Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                is SearchUiState.Idle -> Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Please search username", color = MaterialTheme.colorScheme.onBackground)
                }

                is SearchUiState.Success -> {
                    val users = (uiState as SearchUiState.Success).users
                    if (users.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("User not found", color = MaterialTheme.colorScheme.onBackground)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(horizontal = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(9.dp),
                            contentPadding = PaddingValues(vertical = 6.dp)
                        ) {
                            items(users) { user ->
                                UserCard(
                                    user = user,
                                    isDarkTheme = isDarkTheme,
                                    onClick = { navController.navigate("profile/${user.login}") })
                            }
                        }
                    }
                }

                is SearchUiState.Error -> Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    NetworkErrorCard(message = (uiState as SearchUiState.Error).message)
                    keyboardController?.hide()
                }
            }
        }
    }
}
