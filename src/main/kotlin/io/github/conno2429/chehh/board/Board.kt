package io.github.conno2429.chehh.board

class Board(
    val width: Int = 8,
    val height: Int = 8,
    val grid: Array<Array<Square?>> = Array(8) { arrayOfNulls(8) },
    var isActive: Boolean = false
)

fun Board.clone(): Board {
    val newGrid = Array(height) { rank ->
        Array(width) { file ->
            grid[rank][file]?.copy()
        }
    }
    return Board(width, height, newGrid, isActive)
}