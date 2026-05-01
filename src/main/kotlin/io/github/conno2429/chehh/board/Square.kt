package io.github.conno2429.chehh.board

import io.github.conno2429.chehh.pieces.Piece

enum class SquareColor { LIGHT, DARK }

data class Square(
    var pieceOn: Piece? = null,
    var color: SquareColor,
    var rank: Int = 0,
    var file: Int = 0
)