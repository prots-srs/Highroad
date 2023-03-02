package ua.com.biz_s.highroad.tictactoe.viewmodels

import android.app.Application
import android.view.View
import androidx.lifecycle.*

enum class BoardCellCode {
    A1, A2, A3, B1, B2, B3, C1, C2, C3
}

enum class Players {
    CROSS, NOUGHT
}

//class TictaktoeViewModel(application: Application) : AndroidViewModel(application) {
class TictaktoeViewModel : ViewModel() {
    private val _boardCells = MutableLiveData<MutableMap<BoardCellCode, Players?>>()
    val boardCells: LiveData<MutableMap<BoardCellCode, Players?>> = _boardCells

    private val _turn = MutableLiveData<Players?>()
    val turn: LiveData<Players?> = _turn

    private val _scoreCross = MutableLiveData<Int>()
    val scoreCross: LiveData<String> = Transformations.map(_scoreCross) { it.toString() }

    private val _scoreNought = MutableLiveData<Int>()
    val scoreNought: LiveData<String> = Transformations.map(_scoreNought) { it.toString() }

    private val boardState = mutableMapOf<BoardCellCode, Players?>()
    private var run: Boolean = false
    private var score = mutableMapOf<Players, Int>(
        Players.CROSS to 0,
        Players.NOUGHT to 0
    )
    private var lastWin: Players? = null

    init {
        clearBoard()

        _scoreCross.value = score[Players.CROSS]
        _scoreNought.value = score[Players.NOUGHT]
        lastWin = null
    }

    fun clearBoard() {
        for (cellCode in BoardCellCode.values()) {
            boardState[cellCode] = null
        }
        _boardCells.value = boardState

        if (lastWin == null) {
            _turn.value = Players.CROSS
        } else {
            _turn.value = lastWin
        }

        run = true
    }

    fun onSelectCell(cellCode: BoardCellCode) {
        if (boardState[cellCode] != null || !run) {
            return
        }
        boardState[cellCode] = _turn.value
        _boardCells.value = boardState

        checkEnd()
        changeTurn()
    }

    private fun changeTurn() {
        if (run) {
            if (_turn.value == Players.CROSS) {
                _turn.value = Players.NOUGHT
            } else {
                _turn.value = Players.CROSS
            }
        } else {
            _turn.value = null

        }
    }

    fun checkEnd() {
        var winner: Players? = null

        if (boardState[BoardCellCode.A1] == boardState[BoardCellCode.A2] && boardState[BoardCellCode.A1] == boardState[BoardCellCode.A3]) {
            winner = boardState[BoardCellCode.A1]
        }
        if (boardState[BoardCellCode.B1] == boardState[BoardCellCode.B2] && boardState[BoardCellCode.B1] == boardState[BoardCellCode.B3]) {
            winner = boardState[BoardCellCode.B1]
        }
        if (boardState[BoardCellCode.C1] == boardState[BoardCellCode.C2] && boardState[BoardCellCode.C2] == boardState[BoardCellCode.C3]) {
            winner = boardState[BoardCellCode.C1]
        }
        if (boardState[BoardCellCode.A1] == boardState[BoardCellCode.B1] && boardState[BoardCellCode.A1] == boardState[BoardCellCode.C1]) {
            winner = boardState[BoardCellCode.A1]
        }
        if (boardState[BoardCellCode.A2] == boardState[BoardCellCode.B2] && boardState[BoardCellCode.A2] == boardState[BoardCellCode.C2]) {
            winner = boardState[BoardCellCode.A2]
        }
        if (boardState[BoardCellCode.A3] == boardState[BoardCellCode.B3] && boardState[BoardCellCode.A3] == boardState[BoardCellCode.C3]) {
            winner = boardState[BoardCellCode.A3]
        }
        if (boardState[BoardCellCode.A1] == boardState[BoardCellCode.B2] && boardState[BoardCellCode.A1] == boardState[BoardCellCode.C3]) {
            winner = boardState[BoardCellCode.A1]
        }
        if (boardState[BoardCellCode.A3] == boardState[BoardCellCode.B2] && boardState[BoardCellCode.A3] == boardState[BoardCellCode.C1]) {
            winner = boardState[BoardCellCode.A3]
        }

        if (winner != null) {
            score[winner] = score[winner]!!.plus(1)
            run = false
            lastWin= winner
        }
        if (winner == Players.CROSS) {
            _scoreCross.value = score[winner]
        } else if (winner == Players.NOUGHT) {
            _scoreNought.value = score[winner]
        }
    }
}

