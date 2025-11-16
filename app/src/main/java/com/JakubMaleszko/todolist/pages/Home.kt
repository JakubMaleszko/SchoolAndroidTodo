package com.JakubMaleszko.todolist.pages

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.JakubMaleszko.todolist.R
import com.JakubMaleszko.todolist.components.TodoListItem
import com.JakubMaleszko.todolist.data.TodoItem
import com.JakubMaleszko.todolist.data.getTodos
import com.JakubMaleszko.todolist.data.saveTodos
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navHostController: NavHostController, context: Context) {
    val todosFlow = remember { getTodos(context) }
    val todos by todosFlow.collectAsState(initial = emptyList())

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }

    Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
        LargeTopAppBar(
            title = { Text("Todo") }, scrollBehavior = scrollBehavior
        )
    }, floatingActionButton = {
        FloatingActionButton(onClick = { showDialog = true }) {
            Icon(
                painter = painterResource(id = R.drawable.edit),
                contentDescription = "Add Todo Button",
            )
        }
    }) { innerPadding ->

        LazyColumn(modifier = Modifier.padding(innerPadding), reverseLayout = true) {
            items(todos, key = { it.id }) { todo ->
                TodoListItem(
                    todo = todo, onCheckedChange = { checked ->
                        val updated =
                            todos.map { if (it.id == todo.id) it.copy(isDone = checked) else it }
                        scope.launch { saveTodos(context, updated) }
                    }, onDelete = {
                        val updated = todos.filter { it.id != todo.id }
                        scope.launch { saveTodos(context, updated) }
                    },
                    modifier = Modifier.animateItem()
                )
            }

        }
    }

    if (showDialog) {
        ModalBottomSheet(
            onDismissRequest = { showDialog = false },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp, vertical = 16.dp),

                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Add todo", style = MaterialTheme.typography.displaySmall)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    label = { Text("Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = newDescription,
                    onValueChange = { newDescription = it },
                    label = { Text("Description") },
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (newTitle.isNotBlank()) {
                            val newTodo = TodoItem(id = (todos.maxOfOrNull { it.id } ?: 0) + 1,
                                title = newTitle,
                                description = newDescription)
                            scope.launch { saveTodos(context, todos + newTodo) }
                            newTitle = ""
                            newDescription = ""
                            showDialog = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                ) {
                    Text("Add", fontSize = 18.sp)
                }
            }
        }

    }
}

