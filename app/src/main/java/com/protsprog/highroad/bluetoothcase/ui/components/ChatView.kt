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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.protsprog.highroad.R
import com.protsprog.highroad.bluetoothcase.BTChatMessages
import com.protsprog.highroad.entrance.ui.theme.EntranceTheme

@Composable
fun ChatView(
    modifier: Modifier = Modifier,
    onClickThrowMessage: (String) -> Unit = {},
    serverDeviceName: String = "",
    serverDeviceAddress: String = "",
    onClickCloseChat: () -> Unit = {},
    listMessages: List<BTChatMessages> = emptyList(),
    messageToSend: String = "",
    changeMessageToSend: (String) -> Unit = {}
) {
    var text by rememberSaveable {
        mutableStateOf("")
    }

    Column {
        if (serverDeviceAddress.isNotEmpty()) {
            Row {
                Text(text = "Selected device: ${if (serverDeviceName.length > 0) serverDeviceName else serverDeviceAddress}")
            }
            Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.padding_medium)))
        }

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(2.2f)
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = messageToSend,
                    minLines = 3,
                    maxLines = 4,
                    onValueChange = { changeMessageToSend(it) },
                    label = { Text("Enter message") }
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = dimensionResource(id = R.dimen.padding_medium))
                    .weight(1f)
                    .width(intrinsicSize = IntrinsicSize.Max),
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onClickThrowMessage(messageToSend) }
                ) {
                    Text(text = "Send")
                }
                Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.padding_small)))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onClickCloseChat
                ) {
                    Text(text = "Exit")
                }
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_small)))
        val deviceListState = rememberLazyListState()
        if (listMessages.isNotEmpty()) {
            Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.padding_medium)))
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small)),
                state = deviceListState
            ) {
                var prevAuthor = listMessages.first().author
                var firstItem = true
                items(listMessages, key = { it.timestamp }) { message ->
                    ChatMessage(
                        message = message.message,
                        author = message.author,
                        time = message.date,
                        showAuthor = if (firstItem) true else prevAuthor != message.author,
                        owner = message.owner
                    )
                    prevAuthor = message.author
                    firstItem = false
                }
            }
        }
    }
}

@Preview(device = "id:pixel")
@Composable
fun ChatViewPreview() {
    EntranceTheme {
        ChatView(
            serverDeviceName = "",//"nokia 3310",
            serverDeviceAddress = "DD:EE:22:55:66:99",
            listMessages = listOf(
                BTChatMessages(
                    author = "motto 32",
                    owner = true,
                    message = "Can you play table tennis?",
                    date = "2023.11.08 23:44",
                    timestamp = 123
                ), BTChatMessages(
                    author = "oppo 35",
                    owner = false,
                    message = "Fuck you, motherfucker",
                    date = "2023.11.08 23:45",
                    timestamp = 125
                ), BTChatMessages(
                    author = "oppo 35",
                    owner = false,
                    message = "I can play chess only",
                    date = "2023.11.09 23:55",
                    timestamp = 135
                )
            )
        )
    }
}
