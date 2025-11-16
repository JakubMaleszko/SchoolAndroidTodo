package com.JakubMaleszko.todolist.data

import kotlinx.serialization.Serializable

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Serializable
data class TodoItem(
    val id: Int,
    val title: String,
    val description: String? = null,
    val isDone: Boolean = false
)


val Context.todoDataStore: DataStore<Preferences> by preferencesDataStore(name = "todos")

suspend fun saveTodos(context: Context, todos: List<TodoItem>) {
    context.todoDataStore.edit { prefs ->
        prefs[stringPreferencesKey("todos")] = Json.encodeToString(todos)
    }
}

fun getTodos(context: Context): Flow<List<TodoItem>> {
    return context.todoDataStore.data.map { prefs ->
        prefs[stringPreferencesKey("todos")]?.let { Json.decodeFromString(it) } ?: emptyList()
    }
}