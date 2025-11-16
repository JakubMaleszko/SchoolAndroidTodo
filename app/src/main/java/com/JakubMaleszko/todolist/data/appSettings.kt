package com.JakubMaleszko.todolist.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.collections.get

enum class DarkModeOption {
    SYSTEM,
    LIGHT,
    DARK
}

@Serializable
data class AppSettings(
    val darkModeOption: DarkModeOption = DarkModeOption.SYSTEM,
    val revertTodoOrder: Boolean = false
)

val Context.settingsDataStore by preferencesDataStore(name = "settings")

private val SETTINGS_KEY = stringPreferencesKey("settings")

suspend fun saveSettings(context: Context, settings: AppSettings) {
    context.settingsDataStore.edit { prefs ->
        prefs[SETTINGS_KEY] = Json.encodeToString(settings)
    }
}

fun getSettings(context: Context): Flow<AppSettings> =
    context.settingsDataStore.data.map { prefs ->
        prefs[SETTINGS_KEY]
            ?.let { Json.decodeFromString<AppSettings>(it) }
            ?: AppSettings()
    }