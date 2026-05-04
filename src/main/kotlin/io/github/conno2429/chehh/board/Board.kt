package io.github.conno2429.chehh.board

import io.github.conno2429.chehh.pieces.Bishop
import io.github.conno2429.chehh.pieces.King
import io.github.conno2429.chehh.pieces.Knight
import io.github.conno2429.chehh.pieces.Pawn
import io.github.conno2429.chehh.pieces.Queen
import io.github.conno2429.chehh.pieces.Rook

class Board(
    val width: Int = 8,
    val height: Int = 8,
    val grid: Array<Array<Square?>> = Array(8) { arrayOfNulls(8) },
    var isActive: Boolean = false
)

fun Board.clone(): Board {
    val newGrid = Array(height) { rank ->
        Array(width) { file ->
            val sq = grid[rank][file] ?: return@Array null
            val newPiece = sq.pieceOn?.let { piece ->
                when (piece) {
                    is Pawn -> Pawn(piece.color, piece.position.copy()).also { it.hasMoved = piece.hasMoved }
                    is Rook -> Rook(piece.color, piece.position.copy()).also { it.hasMoved = piece.hasMoved }
                    is Knight -> Knight(piece.color, piece.position.copy()).also { it.hasMoved = piece.hasMoved }
                    is Bishop -> Bishop(piece.color, piece.position.copy()).also { it.hasMoved = piece.hasMoved }
                    is Queen -> Queen(piece.color, piece.position.copy()).also { it.hasMoved = piece.hasMoved }
                    is King -> King(piece.color, piece.position.copy()).also { it.hasMoved = piece.hasMoved }
                    else -> null
                }
            }
            sq.copy(pieceOn = newPiece)
        }
    }
    return Board(width, height, newGrid, isActive)
}