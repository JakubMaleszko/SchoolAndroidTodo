package com.JakubMaleszko.todolist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.JakubMaleszko.todolist.data.AppSettings
import com.JakubMaleszko.todolist.data.getSettings
import com.JakubMaleszko.todolist.pages.HomeScreen
import com.JakubMaleszko.todolist.pages.SettingsScreen
import com.JakubMaleszko.todolist.pages.SettingsScreen
import com.JakubMaleszko.todolist.ui.theme.TODOListTheme

object Routes {
    const val Home = "home"
    const val Settings = "settings"
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val appSettings by getSettings(context).collectAsState(initial = AppSettings())

    TODOListTheme(appSettings = appSettings) {
        NavHost(
            navController = navController,
            startDestination = Routes.Home,
        ) {
            composable(Routes.Home) {
                HomeScreen(navController, context)
            }
            composable(Routes.Settings) {
                SettingsScreen(navController, context)
            }
        }
    }
}