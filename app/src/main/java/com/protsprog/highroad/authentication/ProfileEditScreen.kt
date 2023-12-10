package com.protsprog.highroad.authentication

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.ui.components.AppBar

private val layoutStep = 8.dp

@Composable
fun ProfileEditScreen(
    modifier: Modifier = Modifier,
    user: UserState,
    saveError: String = "",
    onNavigateUp: () -> Unit = {},
    onClickSave: (String) -> Unit,
    isError: Boolean = false
) {
    var name: String by rememberSaveable { mutableStateOf(user.name) }
    val isError = saveError.isNotEmpty()

    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        AppBar(
            title = "Edit User`s profile", onBackPressed = onNavigateUp
        )
    }, floatingActionButton = {
        ExtendedFloatingActionButton(
            onClick = { onClickSave(name) },
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
                isError = isError
            )
            AnimatedVisibility(isError) {
                ErrorForInput(text = saveError)
            }
        }
    }
}
