package io.github.conno2429.chehh.ui

import io.github.conno2429.chehh.pieces.Bishop
import io.github.conno2429.chehh.pieces.King
import io.github.conno2429.chehh.pieces.Knight
import io.github.conno2429.chehh.pieces.Pawn
import io.github.conno2429.chehh.pieces.Piece
import io.github.conno2429.chehh.pieces.PieceColor
import io.github.conno2429.chehh.pieces.PlaceHolder
import io.github.conno2429.chehh.pieces.Queen
import io.github.conno2429.chehh.pieces.Rook

fun pieceResource(piece: Piece): String {
    if (piece is PlaceHolder) return ""

    val color = if (piece.color == PieceColor.WHITE) "light" else "dark"
    val name = when (piece) {
        is Pawn -> "pawn"
        is Rook -> "rook"
        is Knight -> "knight"
        is Bishop -> "bishop"
        is Queen -> "queen"
        is King -> "king"
    }
    return "pieces/${name}_${color}.svg"
}