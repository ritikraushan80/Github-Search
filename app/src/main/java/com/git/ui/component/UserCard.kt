package com.git.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.git.data.model.UserSearchItem


/**
 * Created by Ritik on: 29/08/25
 */

@Composable
fun UserCard(
    user: UserSearchItem, isDarkTheme: Boolean, onClick: () -> Unit
) {
    val cardColor = if (isDarkTheme) MaterialTheme.colorScheme.onSecondary
    else MaterialTheme.colorScheme.surface

    Card(
        onClick = onClick, modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
            containerColor = cardColor, contentColor = MaterialTheme.colorScheme.onSurface
        ), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = user.avatarUrl,
                contentDescription = "Avatar",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(70.dp)
                    .clip(CircleShape)
            )
            Text(
                user.login,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ArrowForward, contentDescription = "Open profile"
            )
        }
    }
}