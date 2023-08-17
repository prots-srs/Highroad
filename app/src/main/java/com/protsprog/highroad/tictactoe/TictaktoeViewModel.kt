package com.protsprog.highroad.tictactoe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.protsprog.highroad.HighroadApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

//@Immutable
enum class PlayerType {
    CROSS, NOUGHT
}


private val emptyBoard: Map<String, PlayerType?> = mapOf(
    "11" to null,
    "12" to null,
    "13" to null,
    "21" to null,
    "22" to null,
    "23" to null,
    "31" to null,
    "32" to null,
    "33" to null
)

//@Stable
data class GameUIState(
    val playerTurn: PlayerType? = PlayerType.CROSS,
    val playerWin: PlayerType? = null,
    val isShowClear: Boolean = false,
    val board: Map<String, PlayerType?> = emptyBoard
)

data class PlayersUiState(
    val nameCross: String = "",
    val nameNought: String = "",
    val scoreCross: Int = 0,
    val scoreNought: Int = 0,
)

@HiltViewModel
class TictaktoeViewModel @Inject constructor(
    private val gameRepository: TicTacToeRepository
) : ViewModel() {

    var gameUiState by mutableStateOf(GameUIState())
        private set

    var playersUiState by mutableStateOf(PlayersUiState())
        private set

    init {
        viewModelScope.launch {
            updateDataFromRepository()
        }
    }

    suspend fun updateDataFromRepository() {
        gameRepository.gamePreferences.collect {
            playersUiState = playersUiState.copy(
                nameCross = it.nameCross,
                nameNought = it.nameNought,
                scoreCross = it.scoreCross,
                scoreNought = it.scoreNought
            )
        }
    }

    fun updateUsername(type: PlayerType, input: String) {
        if (type == PlayerType.CROSS) {
            playersUiState = playersUiState.copy(
                nameCross = input
            )

            viewModelScope.launch {
                gameRepository.saveNameCross(input)
            }
        } else if (type == PlayerType.NOUGHT) {
            playersUiState = playersUiState.copy(
                nameNought = input
            )

            viewModelScope.launch {
                gameRepository.saveNameNought(input)
            }
        }

        gameUiState = gameUiState.copy(
            isShowClear = true
        )
    }

    fun clearGame() {
        gameUiState = gameUiState.copy(
            playerTurn = if (gameUiState.playerWin != null) {
                if (gameUiState.playerWin == PlayerType.CROSS) PlayerType.CROSS else PlayerType.NOUGHT
            } else PlayerType.CROSS,
            board = emptyBoard,
            playerWin = null
        )
    }

//    Clear repository
    fun clearPlayerData() {
        viewModelScope.launch {
            gameRepository.clearData()
            updateDataFromRepository()
            gameUiState = gameUiState.copy(
                isShowClear = false
            )
        }
    }

    fun makeTurn(field: String) {
        if (!gameUiState.board.containsKey(field) || gameUiState.playerWin != null) {
            return
        }

        val state: PlayerType? = gameUiState.board.get(field)
        if (state == null) {
            gameUiState = gameUiState.copy(
                playerTurn = if (gameUiState.playerTurn == PlayerType.CROSS) PlayerType.NOUGHT else PlayerType.CROSS,
                board = gameUiState.board.map { (key, value) ->
                    key to if (key == field) gameUiState.playerTurn else value
                }.toMap()
            )
            checkEndGame()
        }
    }

    fun checkEndGame() {

        val allCellCross: List<String> =
            gameUiState.board.filter { it.value == PlayerType.CROSS }.map { (key, _) -> key }

        val allCellNought: List<String> =
            gameUiState.board.filter { it.value == PlayerType.NOUGHT }.map { (key, _) -> key }

        var win: PlayerType? = null
        val row1 = listOf("11", "12", "13")
        val row2 = listOf("21", "22", "23")
        val row3 = listOf("31", "32", "33")
        val col1 = listOf("11", "21", "31")
        val col2 = listOf("12", "22", "32")
        val col3 = listOf("13", "23", "33")
        val diagonalRight = listOf("11", "22", "33")
        val diagonalLeft = listOf("13", "22", "31")
//        check row win
        if (allCellCross.containsAll(row1) || allCellCross.containsAll(row2) || allCellCross.containsAll(
                row3
            )
        ) {
            win = PlayerType.CROSS
        }
        if (allCellNought.containsAll(row1) || allCellNought.containsAll(row2) || allCellNought.containsAll(
                row3
            )
        ) {
            win = PlayerType.NOUGHT
        }
//        check column win
        if (allCellCross.containsAll(col1) || allCellCross.containsAll(col2) || allCellCross.containsAll(
                col3
            )
        ) {
            win = PlayerType.CROSS
        }
        if (allCellNought.containsAll(col1) || allCellNought.containsAll(col2) || allCellNought.containsAll(
                col3
            )
        ) {
            win = PlayerType.NOUGHT
        }
//        check diagonal win
        if (allCellCross.containsAll(diagonalRight) || allCellCross.containsAll(diagonalLeft)) {
            win = PlayerType.CROSS
        }
        if (allCellNought.containsAll(diagonalRight) || allCellNought.containsAll(diagonalLeft)) {
            win = PlayerType.NOUGHT
        }

        win?.let {
            gameUiState = gameUiState.copy(
                playerWin = it,
                playerTurn = null,
                isShowClear = true
            )

            when (win) {
                PlayerType.CROSS -> {
                    val score = playersUiState.scoreCross.inc()

                    playersUiState = playersUiState.copy(
                        scoreCross = score
                    )
                    viewModelScope.launch {
                        gameRepository.saveScoreCross(score)
                    }
                }

                PlayerType.NOUGHT -> {
                    val score = playersUiState.scoreNought.inc()

                    playersUiState = playersUiState.copy(
                        scoreNought = score
                    )
                    viewModelScope.launch {
                        gameRepository.saveScoreNought(score)
                    }
                }
            }
        }
    }

    /*companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HighroadApplication)
                TictaktoeViewModel(application.tictaktoeRepository)
            }
        }
    }*/
}