package ua.com.biz_s.highroad.tictactoe.util

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import ua.com.biz_s.highroad.R
import ua.com.biz_s.highroad.tictactoe.viewmodels.BoardCellCode
import ua.com.biz_s.highroad.tictactoe.viewmodels.Players


@BindingAdapter("app:turn")
fun bindTurn(imageView: ImageView, turn: Players?) {
    when (turn) {
        Players.CROSS -> {
            imageView.setImageResource(R.drawable.tictaktoe_mark_cross)
        }
        Players.NOUGHT -> {
            imageView.setImageResource(R.drawable.tictaktoe_mark_nought)
        }
        else -> {
            imageView.setImageResource(0)
        }
    }
}

@BindingAdapter("app:boardCells", "app:cellCode")
fun defineCell(imageView: ImageView, cells: MutableMap<BoardCellCode, Players>, cellCode: BoardCellCode) {
    when (cells[cellCode]) {
        Players.CROSS -> {
            imageView.setImageResource(R.drawable.tictaktoe_mark_cross)
        }
        Players.NOUGHT -> {
            imageView.setImageResource(R.drawable.tictaktoe_mark_nought)
        }
        else -> {
            imageView.setImageResource(0)
        }
    }
}