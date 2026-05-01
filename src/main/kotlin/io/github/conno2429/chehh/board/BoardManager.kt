package io.github.conno2429.chehh.board

import io.github.conno2429.chehh.GameMode
import io.github.conno2429.chehh.board.Board
import io.github.conno2429.chehh.pieces.Bishop
import io.github.conno2429.chehh.pieces.King
import io.github.conno2429.chehh.pieces.Knight
import io.github.conno2429.chehh.pieces.Pawn
import io.github.conno2429.chehh.pieces.PieceColor
import io.github.conno2429.chehh.pieces.Position
import io.github.conno2429.chehh.pieces.Queen
import io.github.conno2429.chehh.pieces.Rook

object BoardManager {
    val board = Board()

    fun createBoard() {
        if (board.isActive) {
            println("Board is active")
            return
        }

        for (rank in 0 until board.height) {
            for (file in 0 until board.width) {
                board.grid[rank][file] = Square(
                    rank = rank,
                    file = file,
                    color = if ((rank + file) % 2 == 0) SquareColor.DARK else SquareColor.LIGHT
                )
            }
        }

        setPieces(GameMode.STANDARD)
        board.isActive = true
    }

    fun setPieces(mode: GameMode) {
        when (mode) {
            GameMode.STANDARD -> {
                setPawns()
                setPiecesStandard()
            }
            GameMode.NINESIXTY -> println("WIP")
            else -> println("WIP")
        }
    }

    fun setPawns() {
        // keeping separate from the other pieces for Chess960 configuration
        for (rank in 0 until board.height) {
            val pieceColor = when (rank) {
                1 -> PieceColor.WHITE
                6 -> PieceColor.BLACK
                else -> continue
            }

            for (file in 0 until board.width) {
                board.grid[rank][file]?.pieceOn = Pawn(pieceColor, Position(rank, file))
            }
        }
    }

    fun setPiecesStandard() {
        for (rank in 0 until board.height) {
            val pieceColor = when (rank) {
                0 -> PieceColor.WHITE
                7 -> PieceColor.BLACK
                else -> continue
            }

            for (file in 0 until board.width) {
                val piece = when (file) {
                    0, 7 -> Rook(pieceColor, Position(rank, file))
                    1, 6 -> Knight(pieceColor, Position(rank, file))
                    2, 5 -> Bishop(pieceColor, Position(rank, file))
                    3 -> Queen(pieceColor, Position(rank, file))
                    4 -> King(pieceColor, Position(rank, file))
                    else -> continue
                }

                board.grid[rank][file]?.pieceOn = piece
            }
        }
    }

    fun movePiece() {
        // TODO: moves a piece based on user input
    }

    fun printBoard() {
        if (!board.isActive) {
            createBoard()
        }

        for (rank in board.height - 1 downTo 0) {
            for (file in 0 until board.width) {
                val square = board.grid[rank][file]!!
                val piece = square.pieceOn
                if (piece != null) {
                    print("${piece.symbol} ")
                } else {
                    print(" ")
                }
            }
            println()
        }
    }

    fun toNotation(file: Int, rank: Int): String {
        val fileLetter = ('a' + file).toString()
        val rankNumber = (rank + 1).toString()
        return fileLetter + rankNumber  // e.g. file=4, rank=1 -> "e2"
    }

    fun fromNotation(notation: String): Pair<Int, Int> {
        val file = notation[0] - 'a'
        val rank = notation[1].digitToInt() - 1
        return Pair(file, rank)
    }
}