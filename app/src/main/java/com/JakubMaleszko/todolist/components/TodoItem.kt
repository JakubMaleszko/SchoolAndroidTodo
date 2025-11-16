package com.JakubMaleszko.todolist.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.JakubMaleszko.todolist.data.TodoItem
import com.JakubMaleszko.todolist.R

@Composable
fun TodoListItem(
    todo: TodoItem, onCheckedChange: (Boolean) -> Unit, onDelete: () -> Unit, modifier: Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 64.dp)
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = todo.title, style = MaterialTheme.typography.titleLarge
                )
                if(todo.description != "" && todo.description != null) {
                    todo.description.let {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Checkbox
                Checkbox(
                    checked = todo.isDone, onCheckedChange = { onCheckedChange(it) })

                // Delete button
                IconButton(onClick = onDelete) {
                    Icon(
                        painter = painterResource(id = R.drawable.delete),
                        contentDescription = "Delete todo",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}