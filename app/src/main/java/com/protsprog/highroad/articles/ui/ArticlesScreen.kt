package com.protsprog.highroad.articles.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun ArticlesScreen() {
    Column() {
        Text(
            text = "article screen",
            modifier = Modifier
        )
    }
}