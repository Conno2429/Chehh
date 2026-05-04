package io.github.conno2429.chehh.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.conno2429.chehh.board.BoardManager
import io.github.conno2429.chehh.moves.MoveManager
import io.github.conno2429.chehh.pieces.*

val SQUARE_SIZE = 64.dp
val SQUARE_SIZE_PX = 64

@Composable
fun ChessBoard() {
    val key = BoardManager.recomposeKey
    var selectedSquare by remember { mutableStateOf<Position?>(null) }
    var draggedPiece by remember { mutableStateOf<Position?>(null) }
    var dragPosition by remember { mutableStateOf<Offset?>(null) }
    var validMoves by remember { mutableStateOf<List<Position>>(emptyList()) }

    fun onSquareClick(rank: Int, file: Int) {
        val clicked = Position(rank, file)
        val piece = BoardManager.board.grid[rank][file]?.pieceOn

        when {
            piece != null && piece.color == BoardManager.currentPlayerColor -> {
                selectedSquare = clicked
                validMoves = MoveManager.getValidMoves(piece, BoardManager.board)
            }
            clicked in validMoves -> {
                BoardManager.movePiece(selectedSquare!!, clicked)
                selectedSquare = null
                validMoves = emptyList()
            }
            else -> {
                selectedSquare = null
                validMoves = emptyList()
            }
        }
    }

    Column {
        // file letters top
        Row(modifier = Modifier.padding(start = SQUARE_SIZE)) {
            for (file in 0 until 8) {
                Box(modifier = Modifier.size(SQUARE_SIZE), contentAlignment = Alignment.Center) {
                    Text(('a' + file).toString(), color = Color.White, fontSize = 12.sp)
                }
            }
        }

        Row {
            // rank numbers left
            Column {
                for (rank in 7 downTo 0) {
                    Box(modifier = Modifier.size(SQUARE_SIZE), contentAlignment = Alignment.Center) {
                        Text("${rank + 1}", color = Color.White, fontSize = 12.sp)
                    }
                }
            }

            // board with drag detection
            Box(
                modifier = Modifier.pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            val file = (offset.x / SQUARE_SIZE_PX).toInt()
                            val rank = 7 - (offset.y / SQUARE_SIZE_PX).toInt()
                            val piece = BoardManager.board.grid[rank][file]?.pieceOn
                            if (piece != null && piece.color == BoardManager.currentPlayerColor) {
                                draggedPiece = Position(rank, file)
                                dragPosition = offset
                                selectedSquare = Position(rank, file)
                                validMoves = MoveManager.getValidMoves(piece, BoardManager.board)
                            }
                        },
                        onDrag = { change, _ ->
                            dragPosition = change.position
                        },
                        onDragEnd = {
                            dragPosition?.let { offset ->
                                val file = (offset.x / SQUARE_SIZE_PX).toInt().coerceIn(0, 7)
                                val rank = (7 - (offset.y / SQUARE_SIZE_PX).toInt()).coerceIn(0, 7)
                                val target = Position(rank, file)
                                if (target in validMoves) {
                                    BoardManager.movePiece(selectedSquare!!, target)
                                }
                            }
                            draggedPiece = null
                            dragPosition = null
                            selectedSquare = null
                            validMoves = emptyList()
                        },
                        onDragCancel = {
                            draggedPiece = null
                            dragPosition = null
                            selectedSquare = null
                            validMoves = emptyList()
                        }
                    )
                }
            ) {
                Column {
                    for (rank in 7 downTo 0) {
                        Row {
                            for (file in 0 until 8) {
                                val square = BoardManager.board.grid[rank][file]
                                val isLight = (rank + file) % 2 != 0
                                val isSelected = selectedSquare == Position(rank, file)
                                val isValidMove = Position(rank, file) in validMoves

                                Box(
                                    modifier = Modifier
                                        .size(SQUARE_SIZE)
                                        .background(
                                            when {
                                                isSelected -> Color(0x99FFFF00)
                                                isLight -> Color(0xFFF0D9B5)
                                                else -> Color(0xFFB58863)
                                            }
                                        )
                                        .clickable { onSquareClick(rank, file) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    // piece — hide if being dragged
                                    if (draggedPiece != Position(rank, file)) {
                                        square?.pieceOn?.let { piece ->
                                            val resource = pieceResource(piece)
                                            if (resource.isNotEmpty()) {
                                                Image(
                                                    painter = painterResource(resource),
                                                    contentDescription = null,
                                                    modifier = Modifier.size(SQUARE_SIZE)
                                                )
                                            }
                                        }
                                    }

                                    // valid move indicator
                                    if (isValidMove) {
                                        Box(
                                            modifier = Modifier
                                                .size(SQUARE_SIZE * 0.35f)
                                                .background(Color(0x55000000), CircleShape)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // floating piece follows cursor during drag
                draggedPiece?.let { pos ->
                    val piece = BoardManager.board.grid[pos.rank][pos.file]?.pieceOn
                    dragPosition?.let { offset ->
                        piece?.let {
                            val resource = pieceResource(it)
                            if (resource.isNotEmpty()) {
                                Image(
                                    painter = painterResource(resource),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(SQUARE_SIZE)
                                        .offset {
                                            IntOffset(
                                                offset.x.toInt() - SQUARE_SIZE_PX / 2,
                                                offset.y.toInt() - SQUARE_SIZE_PX / 2
                                            )
                                        }
                                )
                            }
                        }
                    }
                }
            }

            // rank numbers right
            Column {
                for (rank in 7 downTo 0) {
                    Box(modifier = Modifier.size(SQUARE_SIZE), contentAlignment = Alignment.Center) {
                        Text("${rank + 1}", color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }

        // file letters bottom
        Row(modifier = Modifier.padding(start = SQUARE_SIZE)) {
            for (file in 0 until 8) {
                Box(modifier = Modifier.size(SQUARE_SIZE), contentAlignment = Alignment.Center) {
                    Text(('a' + file).toString(), color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}