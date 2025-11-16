package com.JakubMaleszko.todolist.pages

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.JakubMaleszko.todolist.R
import com.JakubMaleszko.todolist.data.AppSettings
import com.JakubMaleszko.todolist.data.DarkModeOption
import com.JakubMaleszko.todolist.data.getSettings
import com.JakubMaleszko.todolist.data.saveSettings
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navHostController: NavHostController, context: Context) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()

    // Read current settings
    val appSettings by getSettings(context).collectAsState(initial = AppSettings())
    var showDarkModeModal by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Settings") },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = "Back button"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            // Dark mode option
            ListItem(
                modifier = Modifier.clickable { showDarkModeModal = true },
                headlineContent = { Text("App theme") },
                trailingContent = { Text(appSettings.darkModeOption.name.lowercase()) }
            )

            // Revert todo order switch
            ListItem(
                headlineContent = { Text("Revert todo order") },
                trailingContent = {
                    Switch(
                        checked = appSettings.revertTodoOrder,
                        onCheckedChange = { isChecked ->
                            scope.launch {
                                saveSettings(context, appSettings.copy(revertTodoOrder = isChecked))
                            }
                        }
                    )
                }
            )
        }

        // Modal for dark mode selection
        if (showDarkModeModal) {
            ModalBottomSheet(onDismissRequest = { showDarkModeModal = false }) {
                val radioOptions = listOf(DarkModeOption.SYSTEM, DarkModeOption.LIGHT, DarkModeOption.DARK)
                val (selectedOption, onOptionSelected) = remember { mutableStateOf(appSettings.darkModeOption) }

                Column(Modifier.selectableGroup()) {
                    radioOptions.forEach { option ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .selectable(
                                    selected = (option == selectedOption),
                                    onClick = {
                                        onOptionSelected(option)
                                        scope.launch {
                                            // Save the new darkModeOption, preserve other settings
                                            saveSettings(context, appSettings.copy(darkModeOption = option))
                                        }
                                    },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = option.name.lowercase(),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                            RadioButton(
                                selected = (option == selectedOption),
                                onClick = null
                            )
                        }
                    }
                }
            }
        }
    }
}