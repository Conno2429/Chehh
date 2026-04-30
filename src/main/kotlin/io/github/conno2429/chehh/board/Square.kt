package io.github.conno2429.chehh.board

data class Square(
    var isOccupied: Boolean = false,
    var pieceOn: Any? = null,
    var color: String? = null,
    var row: Int = 0,
    var column: Int = 0
)