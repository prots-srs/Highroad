package com.protsprog.highroad.tictactoe

/*
TO READ
https://developer.android.com/reference/kotlin/androidx/compose/ui/Modifier
https://developer.android.com/reference/kotlin/androidx/compose/ui/layout/package-summary#layout_1
https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary
https://developer.android.com/jetpack/compose/designsystems/material3
https://developer.android.com/jetpack/compose/text/user-input

https://medium.com/androiddevelopers/effective-state-management-for-textfield-in-compose-d6e5b070fbe5
https://medium.com/@banmarkovic/jetpack-compose-bottom-border-8f1662c2aa84

https://m3.material.io/components
https://m3.material.io/theme-builder

https://betterprogramming.pub/jetpack-compose-theming-colors-1cf86754d5b9
https://foso.github.io/Jetpack-Compose-Playground/foundation/shape/
https://stackoverflow.com/questions/67708713/equal-width-height-in-jetpack-compose-box
 */
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.protsprog.highroad.tictactoe.ui.components.GameBoard
import com.protsprog.highroad.tictactoe.ui.components.PlayerField
import com.protsprog.highroad.tictactoe.ui.components.RestartButton
import com.protsprog.highroad.tictactoe.ui.theme.TicTacToeTheme

@Preview(showBackground = true, widthDp = 375, heightDp = 790)
@Composable
fun PreviewVerticalTicTacToeScreen() {
    TicTacToeTheme {
        TicTacToeScreen()
    }
}

/*
@Preview(showBackground = true, heightDp = 375, widthDp = 790)//840
@Composable
fun PreviewHorizontalTicTacToeScreen() {
    TicTacToeTheme {
        TicTacToeScreen()
    }
}
 */

@Composable
fun TicTacToeScreen(
    modifier: Modifier = Modifier,
    viewModel: TictaktoeViewModel = viewModel(factory = TictaktoeViewModel.Factory)
) {
    val gameUiState = viewModel.gameUiState
    val playersUiState = viewModel.playersUiState

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(
                state = rememberScrollableState { delta -> delta },
                orientation = Orientation.Vertical
            ),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .padding(all = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (gameUiState.playerWin == PlayerType.CROSS) {
                    Row(
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Player ${playersUiState.nameCross} won",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                } else if (gameUiState.playerWin == PlayerType.NOUGHT) {
                    Row(
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Player ${playersUiState.nameNought} won",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                } else {
                    Text(" ")
                }
                RestartButton(onClickRestart = viewModel::clearGame)
            }
            Row(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.tertiaryContainer,
                        RoundedCornerShape(8.dp)
                    )
                    .fillMaxWidth()
                    .padding(all = 16.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                PlayerType.values().forEach { type ->
                    PlayerField(
                        type = { type },
                        name = if (type == PlayerType.CROSS) playersUiState.nameCross else playersUiState.nameNought,
                        score = if (type == PlayerType.CROSS) playersUiState.scoreCross.toString() else playersUiState.scoreNought.toString(),
                        turn = { type == gameUiState.playerTurn },
                        onValueChange = { username ->
                            viewModel.updateUsername(
                                type = type,
                                input = username
                            )
                        }
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                AnimatedVisibility(
                    visible = gameUiState.isShowClear
                ) {
                    Button(onClick = viewModel::clearPlayerData) {
                        Text("Clear game store")
                    }
                }
            }

            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .background(
                        MaterialTheme.colorScheme.tertiaryContainer,
                        RoundedCornerShape(8.dp)
                    )
                    .fillMaxWidth()
                    .padding(all = 8.dp)
            ) {
                GameBoard(
                    state = gameUiState.board,
                    onTurn = { field -> viewModel.makeTurn(field) }
                )
            }
        }
    }
}