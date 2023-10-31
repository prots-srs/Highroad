package com.protsprog.highroad

import androidx.compose.ui.unit.Density
import androidx.test.core.app.ApplicationProvider
import com.protsprog.highroad.motioncase.DIRECTION
import com.protsprog.highroad.motioncase.FifteenPuzzleViewModel
import org.junit.Test

class FifteenPuzzleTest {
    private val viewModel = FifteenPuzzleViewModel()

    @Test
    fun shake_board_Test() {
//        viewModel.shuffleBoard()
    }

/*
    @Test
    fun check_define_approve_move_chip() {
        println("-")
//        println(viewModel.stateUIPointPosition.toString())
        println("a3 move right")
        var move = viewModel.checkApproveMove("a3", DIRECTION.RIGHT)
        println(move.toString())
        println("c4 move down")
        move = viewModel.checkApproveMove("c4", DIRECTION.DOWN)
        println(move.toString())
        println("-")
        println("d3 move right")
        move = viewModel.checkApproveMove("d3", DIRECTION.RIGHT)
        println(move.toString())
        println("-")
        println("d3 move left")
        move = viewModel.checkApproveMove("d3", DIRECTION.LEFT)
        println(move.toString())
        println("-")
    }

    @Test
    fun init_offsets_cells_Test() {
        val density = Density(ApplicationProvider.getApplicationContext())
        viewModel.setOffsetCells(density)
    }*/
}