package com.protsprog.highroad.tictactoe.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.tictactoe.PlayerType
import com.protsprog.highroad.tictactoe.ui.theme.TicTacToeTheme

@Preview(showBackground = true)
@Composable
fun PreviewPlayerFieldCross() {
    TicTacToeTheme {
        PlayerField(
            type = {PlayerType.CROSS},
            name = "Cross",
            score = 12.toString(),
            turn = {true}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPlayerFieldNought() {
    TicTacToeTheme {
        PlayerField(
            type = {PlayerType.NOUGHT},
            name = "Nought",
            score = 17.toString(),
        )
    }
}

@Composable
fun PlayerField(
//    type: PlayerType,
    type: () -> PlayerType,
    name: String = "",
    score: String = "-",
    turn: () -> Boolean = {false},
    onValueChange: (String) -> Unit = {}
) {
    Column(modifier = Modifier.width(170.dp)) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp),
            shape = MaterialTheme.shapes.medium,
            color = if (turn()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Icon(
                    tint = if (turn()) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                    imageVector = if (type() == PlayerType.CROSS) Icons.Outlined.Close else Icons.Outlined.Circle,
                    contentDescription = if (type() == PlayerType.CROSS) "Cross" else "Nought",
                    modifier = Modifier
                        .size(36.dp)
                        .padding(start = 4.dp)
                )

                val focusManager = LocalFocusManager.current
                BasicTextField(
                    modifier = Modifier
                        .width(90.dp)
                        .padding(all = 0.dp),
                    textStyle = TextStyle(
                        color = if (turn()) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    value = name,
                    singleLine = true,
                    onValueChange = onValueChange
                )
                Text(
                    text = score,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(end = 10.dp),
                    color = if (turn()) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                )
            }
        }
        if (turn()) {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = "Your turn",
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}

