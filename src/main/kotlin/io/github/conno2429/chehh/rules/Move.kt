package io.github.conno2429.chehh.rules

import io.github.conno2429.chehh.pieces.Piece
import io.github.conno2429.chehh.pieces.Position

data class Move(
    val piece: Piece,
    val from: Position,
    val to: Position
)
