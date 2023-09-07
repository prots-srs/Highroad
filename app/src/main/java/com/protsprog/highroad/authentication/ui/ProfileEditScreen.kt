package com.protsprog.highroad.authentication.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.UserUIState
import com.protsprog.highroad.ui.components.AppBar

private val layoutStep = 8.dp

@Composable
fun ProfileEditScreen(
    modifier: Modifier = Modifier,
    user: UserUIState,
    onNavigateUp: () -> Unit = {},
    onClickSave: (String) -> Boolean
) {
    var name: String by rememberSaveable { mutableStateOf(user.name) }

    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        AppBar(
            title = "Edit User`s profile", onBackPressed = onNavigateUp
        )
    }, floatingActionButton = {
        ExtendedFloatingActionButton(
            onClick = {
                if(onClickSave(name)) {
                    onNavigateUp()
                }
            },
            icon = { Icon(Icons.Outlined.Save, "Save") },
            text = { Text(text = "Save profile") },
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(all = layoutStep * 4)
        ) {
            Spacer(modifier.height(layoutStep * 2))
            OutlinedTextField(
                modifier = modifier.fillMaxWidth(),
                value = name,
                label = { Text("Name") },
                singleLine = true,
                onValueChange = {
                    name = it
                },
                isError = name.isEmpty()
            )
        }
    }
}
