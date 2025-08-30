package com.git.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.git.data.model.Repo

/**
 * Created by Ritik on: 30/08/25
 */

@Composable
fun RepoItemCard(repo: Repo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSecondary,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(repo.name, style = MaterialTheme.typography.titleMedium)
            Text(repo.description ?: "No description", style = MaterialTheme.typography.bodyMedium)
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("Stars : ${repo.stargazersCount}", modifier = Modifier.weight(1f))
                Text("Forks : ${repo.forksCount}", modifier = Modifier.weight(1f))
            }
        }
    }
}