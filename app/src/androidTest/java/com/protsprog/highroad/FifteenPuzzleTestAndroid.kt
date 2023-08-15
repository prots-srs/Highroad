package com.protsprog.highroad

import androidx.compose.ui.unit.Density
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.test.core.app.ApplicationProvider
import com.protsprog.highroad.motioncase.FifteenPuzzleViewModel
import org.junit.Before
import org.junit.Test

class FifteenPuzzleTestAndroid {
    lateinit private var viewModel: FifteenPuzzleViewModel
//    private val viewModel:FifteenPuzzleViewModel = viewModel()

    @Before
    fun initViewModel() {
        viewModel = FifteenPuzzleViewModel()

        val density = Density(ApplicationProvider.getApplicationContext())
        viewModel.prepareChipOnBoard(density)
    }

    @Test
    fun init_offsets_cells_Test() {
        val density = Density(ApplicationProvider.getApplicationContext())
        viewModel.prepareChipOnBoard(density)
    }
}