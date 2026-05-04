package io.github.conno2429.chehh.board

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.conno2429.chehh.GameManager
import io.github.conno2429.chehh.GameMode
import io.github.conno2429.chehh.board.Board
import io.github.conno2429.chehh.moves.MoveManager
import io.github.conno2429.chehh.moves.MoveRecord
import io.github.conno2429.chehh.pieces.Bishop
import io.github.conno2429.chehh.pieces.King
import io.github.conno2429.chehh.pieces.Knight
import io.github.conno2429.chehh.pieces.Pawn
import io.github.conno2429.chehh.pieces.PieceColor
import io.github.conno2429.chehh.pieces.Position
import io.github.conno2429.chehh.pieces.Queen
import io.github.conno2429.chehh.pieces.Rook

object BoardManager {
    var board = Board()
    var recomposeKey by mutableStateOf(0)
        private set

    val currentPlayerColor get() = GameManager.currentTurn

    private fun notifyChange() {
        recomposeKey++
    }

    fun createBoard() {
        if (board.isActive) {
            println("Board is active")
            return
        }

        for (rank in 0 until board.height) {
            for (file in 0 until board.width) {
                board.grid[rank][file] = Square(
                    rank = rank,
                    file = file,
                    color = if ((rank + file) % 2 == 0) SquareColor.DARK else SquareColor.LIGHT
                )
            }
        }

        board.isActive = true
        notifyChange()
    }

    fun setPieces(mode: GameMode) {
        when (mode) {
            GameMode.STANDARD -> {
                setPawns()
                setPiecesStandard()
            }
            GameMode.NINESIXTY -> println("WIP")
            else -> println("WIP")
        }
        notifyChange()
    }

    fun setPawns() {
        // keeping separate from the other pieces for Chess960 configuration
        for (rank in 0 until board.height) {
            val pieceColor = when (rank) {
                1 -> PieceColor.WHITE
                6 -> PieceColor.BLACK
                else -> continue
            }

            for (file in 0 until board.width) {
                board.grid[rank][file]?.pieceOn = Pawn(pieceColor, Position(rank, file))
            }
        }
    }

    fun setPiecesStandard() {
        for (rank in 0 until board.height) {
            val pieceColor = when (rank) {
                0 -> PieceColor.WHITE
                7 -> PieceColor.BLACK
                else -> continue
            }

            for (file in 0 until board.width) {
                val piece = when (file) {
                    0, 7 -> Rook(pieceColor, Position(rank, file))
                    1, 6 -> Knight(pieceColor, Position(rank, file))
                    2, 5 -> Bishop(pieceColor, Position(rank, file))
                    3 -> Queen(pieceColor, Position(rank, file))
                    4 -> King(pieceColor, Position(rank, file))
                    else -> continue
                }

                board.grid[rank][file]?.pieceOn = piece
            }
        }
    }

    fun movePiece(pos1: Position, pos2: Position) {
        val piece = board.grid[pos1.rank][pos1.file]?.pieceOn ?: return
        val isCapture = board.grid[pos2.rank][pos2.file]?.pieceOn != null

        val candidates = MoveManager.allLegalMoves(piece.color, board)
            .filter { (p, moves) -> p::class == piece::class && pos2 in moves && p != piece }
        val disambigFile = candidates.any { (p, _) -> p.position.file != pos1.file }
        val disambigRank = !disambigFile && candidates.isNotEmpty()

        board.grid[pos1.rank][pos1.file]?.pieceOn = null
        board.grid[pos2.rank][pos2.file]?.pieceOn = piece
        piece.position = pos2
        piece.hasMoved = true

        // handle castling
        val isCastle = piece is King && Math.abs(pos2.file - pos1.file) > 1
        val isKingside = pos2.file > pos1.file
        if (isCastle) {
            val rookFile = if (isKingside) 7 else 0
            val rookTargetFile = if (isKingside) 5 else 3
            val rook = board.grid[pos1.rank][rookFile]?.pieceOn
            board.grid[pos1.rank][rookFile]?.pieceOn = null
            board.grid[pos1.rank][rookTargetFile]?.pieceOn = rook
            rook?.position = Position(pos1.rank, rookTargetFile)
            rook?.hasMoved = true
        }

        val nextColor = if (piece.color == PieceColor.WHITE) PieceColor.BLACK else PieceColor.WHITE
        val record = MoveRecord(
            piece = piece,
            from = pos1,
            to = pos2,
            isCapture = isCapture,
            isCheck = MoveManager.isInCheck(nextColor, board),
            isCheckmate = MoveManager.isCheckmate(nextColor, board),
            disambigFile = disambigFile,
            disambigRank = disambigRank,
            isCastle = isCastle,
            isKingsideCastle = isCastle && isKingside
        )

        GameManager.moves.add(record)
        GameManager.lastMove = record
        notifyChange()
        GameManager.onMoveMade(pos1, pos2)
    }
}