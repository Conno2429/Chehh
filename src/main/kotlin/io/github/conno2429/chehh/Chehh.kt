package io.github.conno2429.chehh

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import io.github.conno2429.chehh.board.BoardManager
import io.github.conno2429.chehh.ui.ChessBoard
import io.github.conno2429.chehh.ui.MoveHistory
import io.github.conno2429.chehh.ui.SetupScreen

fun main() = application {
//    System.setOut(PrintStream(System.out, true, "UTF-8"))
//    GameManager.start()

    Window(onCloseRequest = ::exitApplication,
        title = "Chehh",
        state = WindowState(size = DpSize(1200.dp, 800.dp))) {
        LaunchedEffect(Unit) {
            window.minimumSize = java.awt.Dimension(1200, 540)
            BoardManager.createBoard()
            BoardManager.setPieces(GameMode.STANDARD)
        }

        if (GameManager.gameStarted) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ChessBoard()
                Spacer(modifier = Modifier.width(16.dp))
                MoveHistory(moves = GameManager.moves)
            }
        } else {
            SetupScreen()
        }

        GameManager.gameOverMessage?.let { message ->
            AlertDialog(
                onDismissRequest = {},
                title = { Text(message) },
                buttons = {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = { GameManager.resetGame() }) { Text("New Game") }
                        Button(onClick = { exitApplication() }) { Text("Exit") }
                    }
                }
            )
        }
    }
}