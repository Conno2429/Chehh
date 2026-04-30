package io.github.conno2429.chehh.board

import io.github.conno2429.chehh.board.Board

object BoardManager {
    val board = Board()

    fun createBoard() {
        if (board.isActive) {
            println("Board is active")
            return
        }

        for (column in 0 until board.width) {
            board.grid[column] = mutableMapOf()
            for (row in 0 until board.height) {
                val square = Square()
                square.row = row
                square.column = column

                if ((square.row + square.column) % 2 == 0) {
                    square.color = "dark"
                } else {
                    square.color = "light"
                }

                board.grid[column]?.set(row, square)
            }
        }

        board.isActive = true
    }

    fun setPieces() {
        // TODO: sets the pieces at the beginning of the game
    }

    fun movePiece() {
        // TODO: moves a piece based on user input
    }

    fun printBoard() {
        if (!board.isActive) {
            createBoard()
        }

        for (column in 0 until board.width) {
            for (row in 0 until board.height) {
                print("${board.grid[column]?.get(row)?.color} ")
//                print(board.grid[column]?.get(row)?.column)
//                print("${board.grid[column]?.get(row)?.row} ")
//                print(board.grid)

            }
            println()
        }
    }
}

