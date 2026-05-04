package io.github.conno2429.chehh

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.conno2429.chehh.board.BoardManager
import io.github.conno2429.chehh.board.BoardManager.board
import io.github.conno2429.chehh.moves.MoveManager
import io.github.conno2429.chehh.moves.MoveRecord
import io.github.conno2429.chehh.pieces.PieceColor
import io.github.conno2429.chehh.pieces.Position

enum class GameMode { STANDARD, NINESIXTY }
enum class PlayerMode { ONE, TWO }

object GameManager {
    var gameMode by mutableStateOf<GameMode?>(null)
        private set
    var playerMode by mutableStateOf<PlayerMode?>(null)
        private set
    var playerOne by mutableStateOf<Player?>(null)
        private set
    var playerTwo by mutableStateOf<Player?>(null)
        private set
    var currentTurn by mutableStateOf(PieceColor.WHITE)
        private set
    var gameOverMessage by mutableStateOf<String?>(null)
        private set
    var lastMove: MoveRecord? = null
    var moves = mutableStateListOf<MoveRecord>()
    var turns: Int = 1
    var gameStarted by mutableStateOf(false)
        private set

    fun updateGameMode(mode: GameMode) { gameMode = mode }
    fun updatePlayerMode(mode: PlayerMode) { playerMode = mode }
    fun updatePlayers(p1: Player, p2: Player) { playerOne = p1; playerTwo = p2 }

    fun startGame() {
        BoardManager.createBoard()
        BoardManager.setPieces(gameMode!!)
        gameStarted = true
    }

    fun nextTurn() {
        currentTurn = if (currentTurn == PieceColor.WHITE) PieceColor.BLACK else PieceColor.WHITE
        turns++
    }

    fun onMoveMade(from: Position, to: Position) {
        val nextColor = if (currentTurn == PieceColor.WHITE) PieceColor.BLACK else PieceColor.WHITE

        when {
            MoveManager.isCheckmate(nextColor, board) -> endGame("Checkmate! ${currentTurn.name} wins!")
            MoveManager.isStalemate(nextColor, board) -> endGame("Stalemate! Draw!")
            MoveManager.isInCheck(nextColor, board) -> {
                nextTurn()
                // TODO: show check indicator in UI
            }
            else -> nextTurn()
        }
    }

    fun endGame(message: String) {
        gameOverMessage = message
    }

    fun resetGame() {
        gameOverMessage = null
        currentTurn = PieceColor.WHITE
        turns = 1
        moves.clear()
        lastMove = null
        gameStarted = false
    }
}