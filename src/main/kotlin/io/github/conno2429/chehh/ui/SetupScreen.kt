package io.github.conno2429.chehh.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.conno2429.chehh.GameManager
import io.github.conno2429.chehh.GameMode
import io.github.conno2429.chehh.Player
import io.github.conno2429.chehh.PlayerMode
import io.github.conno2429.chehh.pieces.PieceColor

@Composable
fun SetupScreen() {
    var nameInput by remember { mutableStateOf("") }
    var p2NameInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Chehh", fontSize = 32.sp, color = Color.White)
        Spacer(modifier = Modifier.height(32.dp))

        // game mode
        if (GameManager.gameMode == null) {
            Text("Select Game Mode", color = Color.White, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = { GameManager.updateGameMode(GameMode.STANDARD) }) { Text("Standard") }
                Button(onClick = { GameManager.updateGameMode(GameMode.NINESIXTY) }) { Text("Chess960") }
            }
            return
        }

        // player mode
        if (GameManager.playerMode == null) {
            Text("Select Player Mode", color = Color.White, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = { GameManager.updatePlayerMode(PlayerMode.ONE) }) { Text("1 Player") }
                Button(onClick = { GameManager.updatePlayerMode(PlayerMode.TWO) }) { Text("2 Players") }
            }
            return
        }

        // player names and color
        if (GameManager.playerOne == null) {
            Text("Enter Player Details", color = Color.White, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = nameInput, onValueChange = { nameInput = it }, label = { Text("Player 1 Name") })
            if (GameManager.playerMode == PlayerMode.TWO) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = p2NameInput, onValueChange = { p2NameInput = it }, label = { Text("Player 2 Name") })
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Select Color", color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = {
                    val p1 = Player(nameInput.ifEmpty { "Player 1" }, PieceColor.WHITE)
                    val p2 = if (GameManager.playerMode == PlayerMode.TWO)
                        Player(p2NameInput.ifEmpty { "Player 2" }, PieceColor.BLACK)
                    else Player("Chehh Master", PieceColor.BLACK)
                    GameManager.updatePlayers(p1, p2)
                    GameManager.startGame()
                }) { Text("White") }
                Button(onClick = {
                    val p1 = Player(nameInput.ifEmpty { "Player 1" }, PieceColor.BLACK)
                    val p2 = if (GameManager.playerMode == PlayerMode.TWO)
                        Player(p2NameInput.ifEmpty { "Player 2" }, PieceColor.WHITE)
                    else Player("Chehh Master", PieceColor.WHITE)
                    GameManager.updatePlayers(p1, p2)
                    GameManager.startGame()
                }) { Text("Black") }
                Button(onClick = {
                    val color = PieceColor.entries.random()
                    val p1 = Player(nameInput.ifEmpty { "Player 1" }, color)
                    val p2 = if (GameManager.playerMode == PlayerMode.TWO)
                        Player(p2NameInput.ifEmpty { "Player 2" }, if (color == PieceColor.WHITE) PieceColor.BLACK else PieceColor.WHITE)
                    else Player("Chehh Master", if (color == PieceColor.WHITE) PieceColor.BLACK else PieceColor.WHITE)
                    GameManager.updatePlayers(p1, p2)
                    GameManager.startGame()
                }) { Text("Random") }
            }
            return
        }
    }
}