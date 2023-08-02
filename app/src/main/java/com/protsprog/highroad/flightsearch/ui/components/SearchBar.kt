package com.protsprog.highroad.flightsearch.ui.components

/*
TO READ
https://semicolonspace.com/jetpack-compose-material3-textfield/
 */
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults.colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.protsprog.highroad.flightsearch.ui.theme.FlightSearchTheme

@Composable
fun SearchBar(
    inputText: String = "",
    onValueChange: (String) -> Unit = {},
    onValueClear: () -> Unit = {}
) {
    val contextForToast = LocalContext.current.applicationContext
    val focusManager = LocalFocusManager.current
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = inputText,
        onValueChange = onValueChange,
        label = { Text("Enter departure airport") },
        leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = "Flight search") },
        trailingIcon = if (inputText.isNotEmpty()) {
            {
                Icon(
                    modifier = Modifier.clickable {
                        onValueClear()
                        focusManager.clearFocus()
                    },
                    imageVector = Icons.Outlined.Close, contentDescription = null
                )
            }
        } else {
            null
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus()

                Toast.makeText(
                    contextForToast,
                    "Search text: $inputText",
                    Toast.LENGTH_SHORT
                ).show()
            }
        ),
        singleLine = true,
        shape = CircleShape,
        colors = colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondary,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun SearchBarPreview() {
    FlightSearchTheme {
        SearchBar()
    }
}