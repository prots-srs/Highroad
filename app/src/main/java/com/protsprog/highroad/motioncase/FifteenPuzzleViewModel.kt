package com.protsprog.highroad.motioncase

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.protsprog.highroad.motioncase.ui.components.CHIP_SIZE
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.random.Random

enum class DIRECTION {
    UP, RIGHT, LEFT, DOWN
}

class FifteenPuzzleViewModel : ViewModel() {
    //board web
    private lateinit var boardWeb: List<String> //in init

    //chip -> board web
    private lateinit var chipsOnBoard: MutableMap<Int, String>//in init

    //board web -> offset
    private lateinit var boardWebOffsets: Map<String, IntOffset> //need device density

    //chip -> offset
//    var stateUIChipsOnBoard: Map<Int, IntOffset> by mutableStateMapOf() // after init boardWebOffsets
    var stateUIChipsOnBoard: Map<Int, IntOffset> by mutableStateOf(emptyMap()) // after init boardWebOffsets

    //    game runtime
    var stateUIBlockingChip: Int by mutableStateOf(0)
    var stateUIEndGame: Boolean by mutableStateOf(false)
    private var widthChip: Int = 0

    init {
        defineBoardWeb()
//        and
        shuffleBoard()
    }

    private fun defineBoardWeb() {
        val cells = listOf<String>().toMutableList()
        for (rowNumber in 'a'..'d') {
            for (colNumber in 1..4) {
                cells.add("$rowNumber$colNumber")
            }
        }
        boardWeb = cells.toList()
    }

    fun prepareChipOnBoard(density: Density) {
        defineBoardWebOffsets(density)
        positionChipsOnBoard()
    }

    private fun defineBoardWebOffsets(density: Density) {
        with(density) {
            widthChip = CHIP_SIZE.roundToPx()
        }
        boardWebOffsets = boardWeb.associateWith { cell ->
            val offsetX: Float
            val offsetY: Float

            with(density) {
                offsetX = (CHIP_SIZE * cell[1].digitToInt().dec()).toPx()
                offsetY = when (cell[0]) {
                    'b' -> CHIP_SIZE.toPx()
                    'c' -> (CHIP_SIZE * 2).toPx()
                    'd' -> (CHIP_SIZE * 3).toPx()
                    else -> 0f
                }
            }

            IntOffset(offsetX.roundToInt(), offsetY.roundToInt())
        }.toMap()
    }

    private fun shuffleBoard() {
        val newOrder: MutableList<Int> =
            (1..boardWeb.size.dec()).shuffled(random = Random).toMutableList()
        newOrder.add(0)
        chipsOnBoard = boardWeb.mapIndexed { index, cell ->
            newOrder[index] to cell
        }.toMap() as MutableMap<Int, String>
    }

    fun restartGame() {
        stateUIBlockingChip = 0
        stateUIEndGame = false
        shuffleBoard()
        positionChipsOnBoard()
    }

    private fun positionChipsOnBoard() {
        stateUIChipsOnBoard = chipsOnBoard.map { cell ->
            val offset: IntOffset = boardWebOffsets[cell.value] ?: IntOffset(0, 0)
            cell.key to offset
        }.toMap()
    }

    fun onChangeOffset(chip: Int, dragAmount: Offset) {
//        check blocking chip moved
        if ((stateUIBlockingChip > 0 && stateUIBlockingChip != chip) || stateUIEndGame) {
            return
        }
        stateUIChipsOnBoard[chip]?.let { stateChipOffset ->
            boardWebOffsets[chipsOnBoard[chip]]?.let { placeChipOnBoardOffset ->

                var offsetX: Int = stateChipOffset.x
                var offsetY: Int = stateChipOffset.y

                var saveOffset = false

                if (dragAmount.x.absoluteValue > dragAmount.y.absoluteValue) {//horizontal move
                    val leftBound = placeChipOnBoardOffset.x - widthChip
                    val rightBound = placeChipOnBoardOffset.x + widthChip
                    // offset to save
                    offsetX = stateChipOffset.x + dragAmount.x.roundToInt()
                    if (offsetX > rightBound) {
                        offsetX = rightBound
                    } else if (offsetX < leftBound) {
                        offsetX = leftBound
                    }

                    if (dragAmount.x > 0) {//right move
                        if (offsetX > placeChipOnBoardOffset.x) {
                            if (isNextToPlaceFree(chip, DIRECTION.RIGHT) && offsetX <= rightBound) {
                                saveOffset = true
                                stateUIBlockingChip = chip
                            }
                        } else if (offsetX < placeChipOnBoardOffset.x) {
                            saveOffset = true
                            stateUIBlockingChip = chip
                        }
                    } else { //left move
                        if (offsetX < placeChipOnBoardOffset.x) {
                            if (isNextToPlaceFree(chip, DIRECTION.LEFT) && leftBound <= offsetX) {
                                saveOffset = true
                                stateUIBlockingChip = chip
                            }
                        } else if (offsetX > placeChipOnBoardOffset.x) {
                            saveOffset = true
                            stateUIBlockingChip = chip
                        }
                    }

                    if (offsetX == rightBound || offsetX == leftBound) {
                        stateUIBlockingChip = 0
                        placeChipOnEmptyPlace(chip)
                    } else if (offsetX == placeChipOnBoardOffset.x) { //move on himself place
                        stateUIBlockingChip = 0
                    }

                } else {//vertical move
                    val topBound = placeChipOnBoardOffset.y - widthChip
                    val bottomBound = placeChipOnBoardOffset.y + widthChip

                    offsetY = stateChipOffset.y + dragAmount.y.roundToInt()
                    if (offsetY > bottomBound) {
                        offsetY = bottomBound
                    } else if (offsetY < topBound) {
                        offsetY = topBound
                    }

                    if (dragAmount.y > 0) {//down move
                        if (offsetY > placeChipOnBoardOffset.y) {
                            if (isNextToPlaceFree(chip, DIRECTION.DOWN) && offsetY <= bottomBound) {
                                saveOffset = true
                                stateUIBlockingChip = chip
                            }
                        } else if (offsetY < placeChipOnBoardOffset.y) {
                            saveOffset = true
                            stateUIBlockingChip = chip
                        }
                    } else { //up move
                        if (offsetY < placeChipOnBoardOffset.y) {
                            if (isNextToPlaceFree(chip, DIRECTION.UP) && topBound <= offsetY) {
                                saveOffset = true
                                stateUIBlockingChip = chip
                            }
                        } else if (offsetY > placeChipOnBoardOffset.y) {
                            saveOffset = true
                            stateUIBlockingChip = chip
                        }
                    }

                    if (offsetY == topBound || offsetY == bottomBound) {
                        stateUIBlockingChip = 0
                        placeChipOnEmptyPlace(chip)
                    } else if (offsetY == placeChipOnBoardOffset.y) { //move on himself place
                        stateUIBlockingChip = 0
                    }
                }

                if (saveOffset) {
                    saveStateChipOffset(chip, IntOffset(offsetX, offsetY))
                }
            }
        }
    }

    private fun saveStateChipOffset(chip: Int, offset: IntOffset) {
        viewModelScope.launch {
            stateUIChipsOnBoard = stateUIChipsOnBoard.map { chipOnBoard ->
                if (chipOnBoard.key == chip) {
                    chipOnBoard.key to offset
                } else {
                    chipOnBoard.key to chipOnBoard.value
                }
            }.toMap()

            checkEndGame()
        }
    }

    private fun checkEndGame() {
        stateUIEndGame = false

        var itsPlaces = 0
        chipsOnBoard.forEach { chip ->
            when (chip.key) {
                0 -> if (chip.value == "d4") itsPlaces = itsPlaces.inc()
                1 -> if (chip.value == "a1") itsPlaces = itsPlaces.inc()
                2 -> if (chip.value == "a2") itsPlaces = itsPlaces.inc()
                3 -> if (chip.value == "a3") itsPlaces = itsPlaces.inc()
                4 -> if (chip.value == "a4") itsPlaces = itsPlaces.inc()
                5 -> if (chip.value == "b1") itsPlaces = itsPlaces.inc()
                6 -> if (chip.value == "b2") itsPlaces = itsPlaces.inc()
                7 -> if (chip.value == "b3") itsPlaces = itsPlaces.inc()
                8 -> if (chip.value == "b4") itsPlaces = itsPlaces.inc()
                9 -> if (chip.value == "c1") itsPlaces = itsPlaces.inc()
                10 -> if (chip.value == "c2") itsPlaces = itsPlaces.inc()
                11 -> if (chip.value == "c3") itsPlaces = itsPlaces.inc()
                12 -> if (chip.value == "c4") itsPlaces = itsPlaces.inc()
                13 -> if (chip.value == "d1") itsPlaces = itsPlaces.inc()
                14 -> if (chip.value == "d2") itsPlaces = itsPlaces.inc()
                15 -> if (chip.value == "d3") itsPlaces = itsPlaces.inc()
                else -> {}
            }
        }
        if (itsPlaces == chipsOnBoard.size) {
            stateUIEndGame = true
        }
    }

    private fun isNextToPlaceFree(chip: Int, move: DIRECTION): Boolean {
        var placeFree = false

        getNextToChipPlace(chip, move)?.let { nextToChip ->
            val emptyPlace = chipsOnBoard[0]
            if (nextToChip == emptyPlace) {
                placeFree = true
            }
        }

        return placeFree
    }

    private fun getNextToChipPlace(chip: Int, move: DIRECTION): String? {
        var nextToChip: String? = null

        val derivePlace = chipsOnBoard[chip]
        derivePlace?.let {
            val rowDerive = derivePlace[0]
            val columnDerive = derivePlace[1].digitToInt()

            //        define sibling place
            val rowDestination: String?
            val columnDestination: Int?
            when (move) {
                DIRECTION.UP -> {
                    rowDestination =
                        when (rowDerive) {
                            'b' -> "a"
                            'c' -> "b"
                            'd' -> "c"
                            else -> null
                        }
                    columnDestination = columnDerive
                }

                DIRECTION.DOWN -> {
                    rowDestination =
                        when (rowDerive) {
                            'a' -> "b"
                            'b' -> "c"
                            'c' -> "d"
                            else -> null
                        }
                    columnDestination = columnDerive
                }

                DIRECTION.RIGHT -> {
                    rowDestination = rowDerive.toString()
                    columnDestination = if (columnDerive < 4) (columnDerive + 1) else null
                }

                DIRECTION.LEFT -> {
                    rowDestination = rowDerive.toString()
                    columnDestination = if (columnDerive > 1) (columnDerive - 1) else null
                }
            }
            if (rowDestination != null && columnDestination != null) {
                nextToChip = "$rowDestination$columnDestination"
            }
        }

        return nextToChip
    }

    private fun placeChipOnEmptyPlace(chip: Int) {

        val freePlace = chipsOnBoard[0]
        val chipPlace = chipsOnBoard[chip]
        freePlace?.let {
            chipsOnBoard[chip] = it
        }
        chipPlace?.let {
            chipsOnBoard[0] = it
        }
    }

}

