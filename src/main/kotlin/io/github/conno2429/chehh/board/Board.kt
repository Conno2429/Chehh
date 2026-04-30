package io.github.conno2429.chehh.board

data class Board(
    val width: Int = 8,
    val height: Int = 8,
    val grid: MutableMap<Int, MutableMap<Int, Square>> = mutableMapOf(),
    var isActive: Boolean = false
)