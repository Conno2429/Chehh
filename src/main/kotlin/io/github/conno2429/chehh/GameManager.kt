package io.github.conno2429.chehh

import io.github.conno2429.chehh.board.BoardManager
import io.github.conno2429.chehh.pieces.PieceColor
import io.github.conno2429.chehh.pieces.Position
import kotlin.system.exitProcess

enum class GameMode { STANDARD, NINESIXTY }
enum class PlayerMode { ONE, TWO }

object GameManager {
    private lateinit var gameMode: GameMode
    private lateinit var playerMode: PlayerMode
    private lateinit var playerOne: Player
    private lateinit var playerTwo: Player
    private lateinit var moves: List<Position>

    fun start() {
        println("Time to play Chehh!")

        selectGameMode()
        selectPlayerMode()
        enterName()

        BoardManager.createBoard()
        BoardManager.setPieces(gameMode)

        gameLoop()
    }

    fun selectGameMode() {
        while (true) {
            println("Select game mode: 1) Standard  2) Chess960 \nPress (X) to exit")
            when (readln()) {
                "1" -> { gameMode = GameMode.STANDARD; return }
                "2" -> { gameMode = GameMode.NINESIXTY; return }
                "X" -> exitProcess(0)
                else -> println("Invalid input, try again")
            }
        }
    }

    fun selectPlayerMode() {
        while (true) {
            println("Select player mode: 1) One player  2) Two player \nPress (X) to exit")
            when (readln()) {
                "1" -> { playerMode = PlayerMode.ONE; return }
                "2" -> { playerMode = PlayerMode.TWO; return }
                "X" -> exitProcess(0)
                else -> println("Invalid input, try again")
            }
        }
    }

    fun enterName() {
        when (playerMode) {
            PlayerMode.ONE -> {
                println("Enter username: \nPress (X) to exit")
                val nameInput = readln()
                if (nameInput == "X") exitProcess(0)

                val color = selectColor()
                playerOne = Player(nameInput, color)
                playerTwo = Player("Chehh Master", if (color == PieceColor.WHITE) PieceColor.BLACK else PieceColor.WHITE)
            }
            PlayerMode.TWO -> {
                println("Player 1, enter username: \nPress (X) to exit")
                val p1Input = readln()
                if (p1Input == "X") exitProcess(0)

                println("Player 2, enter username: \nPress (X) to exit")
                val p2Input = readln()
                if (p2Input == "X") exitProcess(0)

                val color = selectColor()
                playerOne = Player(p1Input, color)
                playerTwo = Player(p2Input, if (color == PieceColor.WHITE) PieceColor.BLACK else PieceColor.WHITE)
            }
        }
    }

    private fun selectColor(): PieceColor {
        while (true) {
            println("Select Color: White (W), Black (B), or Random (R) \nPress (X) to exit")
            when (readln()) {
                "White", "W", "w" -> return PieceColor.WHITE
                "Black", "B", "b" -> return PieceColor.BLACK
                "Random", "R", "r" -> return PieceColor.entries.random()
                "X" -> exitProcess(0)
                else -> println("Invalid input, try again")
            }
        }
    }

    fun selectMove() {

    }

    private fun gameLoop() {
        when (playerMode) {
            PlayerMode.ONE -> {

            }
            PlayerMode.TWO -> {

            }
        }
    }
}