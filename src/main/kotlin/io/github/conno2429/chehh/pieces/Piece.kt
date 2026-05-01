package io.github.conno2429.chehh.pieces

enum class PieceColor { WHITE, BLACK }

sealed class Piece(
    val color: PieceColor,
    var position: Position,
) {
    var hasMoved = false
    abstract val value: Int
    abstract val symbol: Char
}

data class Position(val rank: Int, val file: Int)

class Pawn(color: PieceColor, position: Position) : Piece(color, position) {
    override val value = 1
    override val symbol = if (color == PieceColor.WHITE) '♟' else '♙'
}

class Rook(color: PieceColor, position: Position) : Piece(color, position) {
    override val value = 5
    override val symbol = if (color == PieceColor.WHITE) '♜' else '♖'
}

class Knight(color: PieceColor, position: Position) : Piece(color, position) {
    override val value = 3
    override val symbol = if (color == PieceColor.WHITE) '♞' else '♘'
}

class Bishop(color: PieceColor, position: Position) : Piece(color, position) {
    override val value = 3
    override val symbol = if (color == PieceColor.WHITE) '♝' else '♗'
}

class Queen(color: PieceColor, position: Position) : Piece(color, position) {
    override val value = 9
    override val symbol = if (color == PieceColor.WHITE) '♛' else '♕'
}

class King(color: PieceColor, position: Position) : Piece(color, position) {
    override val value = 0
    override val symbol = if (color == PieceColor.WHITE) '♚' else '♔'
}

class PlaceHolder(color: PieceColor, position: Position) : Piece(color, position) {
    override val value = 0
    override val symbol = if (color == PieceColor.WHITE) ' ' else ' '
}
