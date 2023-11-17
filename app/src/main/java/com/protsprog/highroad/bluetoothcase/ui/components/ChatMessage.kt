package com.protsprog.highroad.bluetoothcase.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.protsprog.highroad.R
import com.protsprog.highroad.entrance.ui.theme.EntranceTheme

@Composable
fun ChatMessage(
    modifier: Modifier = Modifier,
    message: String = "",
    author: String = "",
    time: String = "",
    showAuthor: Boolean = false,
    owner: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = if (owner) Alignment.Start else Alignment.End
    ) {
        if (showAuthor) {
            Row {
                val authorText = if (owner) "me" else author
                Text(text = authorText, fontWeight = FontWeight.Bold)
                Text(", ${time}")
            }
            Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.padding_small) / 2))
        }
        Surface(
            modifier = modifier
                .width(IntrinsicSize.Max)
                .padding(top = dimensionResource(id = R.dimen.padding_small) / 4),
            color = if (owner) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = if (owner) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onTertiaryContainer,
            shape = RoundedCornerShape(
                topStart = CornerSize(5),
                topEnd = CornerSize(20),
                bottomStart = CornerSize(20),
                bottomEnd = CornerSize(20),
            )
        ) {
            Row(
                modifier = modifier
                    .padding(all = dimensionResource(id = R.dimen.padding_small))
//                    .width()
//                    .fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    message,
                    fontSize = 16.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Preview(device = "id:pixel_4")
@Composable
fun ChatMessagePreview() {
    EntranceTheme {
        ChatMessage(
            message = "Can you play table tennis?\t\nasd toyi nolley vmiv",
            author = "motto 32",
            time = "2023.11.08 23:44",
            showAuthor = true,
            owner = false
        )
    }
}

@Preview(device = "id:pixel_4")
@Composable
fun ChatMessagePreview2() {
    EntranceTheme {
        ChatMessage(
            message = "Can you play table tennis?",
            author = "motto 32",
            time = "2023.11.08 23:44",
            showAuthor = true,
            owner = true
        )
    }
}