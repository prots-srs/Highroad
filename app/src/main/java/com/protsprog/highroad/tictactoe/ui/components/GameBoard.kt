package com.protsprog.highroad.tictactoe.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.protsprog.highroad.R
import com.protsprog.highroad.tictactoe.PlayerType
import com.protsprog.highroad.tictactoe.ui.theme.TicTacToeTheme

@Preview(showBackground = true)
@Composable
fun PreviewGameField() {
    TicTacToeTheme {
        GameBoard(
            state = mapOf(
                "11" to null,
                "12" to PlayerType.CROSS,
                "13" to null,
                "21" to PlayerType.NOUGHT,
                "22" to PlayerType.CROSS,
                "23" to null,
                "31" to null,
                "32" to PlayerType.CROSS,
                "33" to PlayerType.NOUGHT
            )
        )
    }
}

@Composable
fun GameBoard(
    state: Map<String, PlayerType?>,
    onTurn: (String) -> Unit = {}
) {
    Column {
        for (rowNumber in 1..3) {
            Row {
                for (colNumber in 1..3) {
                    val indexCell = rowNumber.toString() + colNumber.toString()
                    val borderColor = MaterialTheme.colorScheme.primary
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
//                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { onTurn(indexCell) }
                            .drawBehind {
                                val borderSize = 5.dp.toPx()
                                val halfBorderSize = borderSize / 2

//                                top border
                                if (rowNumber == 2 || rowNumber == 3) {
                                    drawLine(
                                        color = borderColor,
                                        start = Offset(0f, halfBorderSize),
                                        end = Offset(size.width, halfBorderSize),
                                        strokeWidth = borderSize
                                    )
                                }
//                                bottom border
                                if (rowNumber == 1 || rowNumber == 2) {
                                    drawLine(
                                        color = borderColor,
                                        start = Offset(0f, size.height - halfBorderSize),
                                        end = Offset(size.width, size.height - halfBorderSize),
                                        strokeWidth = borderSize
                                    )
                                }
//                                start border
                                if (colNumber == 2 || colNumber == 3) {
                                    drawLine(
                                        color = borderColor,
                                        start = Offset(0f + halfBorderSize, 0f),
                                        end = Offset(
                                            0f + halfBorderSize,
                                            size.height
                                        ),
                                        strokeWidth = borderSize
                                    )
                                }

//                                end border
                                if (colNumber == 1 || colNumber == 2) {
                                    drawLine(
                                        color = borderColor,
                                        start = Offset(size.width - halfBorderSize, 0f),
                                        end = Offset(size.width - halfBorderSize, size.height),
                                        strokeWidth = borderSize
                                    )
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {

                        val stateCell = state.getValue(indexCell)
                        if (stateCell == PlayerType.CROSS) {
                            Image(
                                modifier = Modifier
                                    .fillMaxSize(),
                                painter = painterResource(id = R.drawable.tictactoe_mark_cross),
                                contentDescription = ""
                            )
                        } else if (stateCell == PlayerType.NOUGHT) {
                            Image(
                                modifier = Modifier
                                    .padding(all = 16.dp)
                                    .fillMaxSize(),
                                painter = painterResource(id = R.drawable.tictactoe_mark_nought),
                                contentDescription = ""
                            )
                        }
                    }
                }
            }
        }
    }
}

/*
Samples
 */
/*
Column {
    for (i in 0 until 3) {
        Row {
            for (j in 0 until 3) {
//                    val isLightSquare = i % 2 == j % 2
//                    val squareColor = if (isLightSquare) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondaryContainer
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .padding(all = 1.dp)
                        .border(
                            4.dp,
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(8.dp)
                        )
//                            .background(squareColor)
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${i + j}",
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            }
        }
    }
}
*/
/*Box {
    Row(Modifier.height(IntrinsicSize.Min)) {
        Text(
            text = "This is a really short text",
            modifier = Modifier.weight(1f).fillMaxHeight()
        )
        Box(Modifier.width(1.dp).fillMaxHeight().background(MaterialTheme.colorScheme.primary))
        Text(
            text = "This is a much much much much much much much much much much" +
                    " much much much much much much longer text",
            modifier = Modifier.weight(1f).fillMaxHeight()
        )
    }
}*/
/*
    Box {
        Row(Modifier.height(IntrinsicSize.Max)) {
            val modifier = Modifier.fillMaxHeight().weight(1f)
            Box(modifier.aspectRatio(2f).background(MaterialTheme.colorScheme.secondary))
            Box(Modifier.width(1.dp).fillMaxHeight().background(MaterialTheme.colorScheme.primary))
            Box(modifier.aspectRatio(1f).background(MaterialTheme.colorScheme.error))
        }
    }

 */