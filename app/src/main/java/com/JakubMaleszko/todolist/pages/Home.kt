package com.JakubMaleszko.todolist.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.JakubMaleszko.todolist.components.TodoItem
import com.JakubMaleszko.todolist.components.TodoListItem

@Composable
fun HomeScreen() {
    var sampleTodo by remember { mutableStateOf(TodoItem(1, "Buy groceries", "Milk, eggs, bread")) }

    TodoListItem(todo = sampleTodo) { checked ->
        sampleTodo = sampleTodo.copy(isDone = checked)
    }
}