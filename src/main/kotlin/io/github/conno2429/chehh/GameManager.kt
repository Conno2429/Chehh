package io.github.conno2429.chehh

import io.github.conno2429.chehh.board.BoardManager

enum class GameMode { STANDARD, NINESIXTY }
enum class PlayerMode { ONE, TWO }

object GameManager {
    private lateinit var gameMode: GameMode
    private lateinit var playerMode: PlayerMode

    fun start() {
        println("Time to play Chehh!")

        println("Select game mode: 1) Standard  2) Chess960")
        gameMode = when (readln()) {
            "1" -> GameMode.STANDARD
            "2" -> GameMode.NINESIXTY
            else -> GameMode.STANDARD
        }

        println("Select player mode: 1) One player  2) Two player")
        playerMode = when (readln()) {
            "1" -> PlayerMode.ONE
            "2" -> PlayerMode.TWO
            else -> PlayerMode.TWO
        }

        BoardManager.createBoard()
        BoardManager.printBoard()

        gameLoop()
    }

    private fun gameLoop() {
        
    }
}