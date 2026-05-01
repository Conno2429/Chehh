package io.github.conno2429.chehh.rules

import io.github.conno2429.chehh.board.Board
import io.github.conno2429.chehh.board.clone
import io.github.conno2429.chehh.pieces.Bishop
import io.github.conno2429.chehh.pieces.King
import io.github.conno2429.chehh.pieces.Knight
import io.github.conno2429.chehh.pieces.Pawn
import io.github.conno2429.chehh.pieces.Piece
import io.github.conno2429.chehh.pieces.PieceColor
import io.github.conno2429.chehh.pieces.Position
import io.github.conno2429.chehh.pieces.Queen
import io.github.conno2429.chehh.pieces.Rook
import kotlin.math.abs

object MoveManager {
    fun isLegalMove(piece: Piece, target: Position, board: Board): Boolean {
        return true
    }

    private fun generateMoves(piece: Piece, board: Board): List<Position> {
        val raw = when (piece) {
            is Pawn -> pawnMoves(piece, board)
            is Rook -> rookMoves(piece, board)
            is Knight -> knightMoves(piece, board)
            is Bishop -> bishopMoves(piece, board)
            is Queen -> queenMoves(piece, board)
            is King -> kingMoves(piece, board)
        }

        return raw.filter { !leavesKingInCheck(piece, board) }
    }

    private fun pawnMoves(piece: Piece, board: Board): List<Position> {
        val moves = mutableListOf<Position>()
        val position = piece.position

        when (piece.color) {
            PieceColor.WHITE -> {
                // straight line
                val oneForward = Position(position.rank + 1, position.file)
                if (!pieceAt(oneForward, board) && inBounds(oneForward, board)) {
                    moves.add(oneForward)
                }

                // home rank
                val twoForward = Position(position.rank + 2, position.file)
                if (!piece.hasMoved) {
                    if (!pieceAt(twoForward, board) && !pieceAt(oneForward, board)) {
                        moves.add(twoForward)
                    }
                }

                // captures
                for (dFile in listOf(-1, 1)) {
                    val diag = Position(position.rank + 1, position.file + dFile)
                    if (inBounds(diag, board) && enemyAt(diag, piece.color, board)) {
                        moves.add(diag)
                    }
                }

                // en passant
                if (piece.position.rank == 4) {
                    // TODO: add last turn pawn move condition to its sides
                }

                // potential promotion logic, decide where to handle later
            }
            PieceColor.BLACK -> {
                // straight line
                val oneForward = Position(position.rank - 1, position.file)
                if (!pieceAt(oneForward, board) && inBounds(oneForward, board)) {
                    moves.add(oneForward)
                }

                // home rank
                val twoForward = Position(position.rank - 2, position.file)
                if (!piece.hasMoved) {
                    if (!pieceAt(twoForward, board) && !pieceAt(oneForward, board)) {
                        moves.add(twoForward)
                    }
                }

                // captures
                for (dFile in listOf(-1, 1)) {
                    val diag = Position(position.rank - 1, position.file + dFile)
                    if (inBounds(diag, board) && enemyAt(diag, piece.color, board)) {
                        moves.add(diag)
                    }
                }

                // en passant
                if (piece.position.rank == 3) {
                    // TODO: add last turn pawn move condition to its sides
                }

                // potential promotion logic, decide where to handle later
            }
        }

        return moves
    }

    private fun knightMoves(piece: Piece, board: Board): List<Position> {
        val (rank, file) = piece.position
        val candidates = listOf(
            Position(rank + 2, file + 1),
            Position(rank + 2, file - 1),
            Position(rank - 2, file + 1),
            Position(rank - 2, file - 1),
            Position(rank + 1, file + 2),
            Position(rank + 1, file - 2),
            Position(rank - 1, file + 2),
            Position(rank - 1, file - 2),
        )
        return candidates.filter { inBounds(it, board) && !friendlyAt(it, piece.color, board) }
    }

    private fun rookMoves(piece: Piece, board: Board): List<Position> {
        return slidingMoves(piece, board, listOf(
            Pair(1, 0), Pair(-1, 0),
            Pair(0, 1), Pair(0, -1)
        ))
    }

    private fun bishopMoves(piece: Piece, board: Board): List<Position> {
        return slidingMoves(piece, board, listOf(
            Pair(1, 1), Pair(1, -1),
            Pair(-1, 1), Pair(-1, -1)
        ))
    }

    private fun queenMoves(piece: Piece, board: Board): List<Position> {
        return rookMoves(piece, board) + bishopMoves(piece, board)
    }

    fun kingMoves(piece: Piece, board: Board): List<Position> {
        val (rank, file) = piece.position
        val candidates = listOf(
            Position(rank + 1, file),
            Position(rank - 1, file),
            Position(rank, file + 1),
            Position(rank, file - 1),
            Position(rank + 1, file + 1),
            Position(rank + 1, file - 1),
            Position(rank - 1, file + 1),
            Position(rank - 1, file - 1),
        )

        val enemyKingPos = findKing(piece.color, board)?.position

        return candidates.filter {
            inBounds(it, board)
                    && !friendlyAt(it, piece.color, board)
                    && (enemyKingPos == null || !isAdjacent(it, enemyKingPos))
        }
    }

    private fun slidingMoves(piece: Piece, board: Board, directions: List<Pair<Int, Int>>): List<Position> {
        val moves = mutableListOf<Position>()
        for ((dRank, dFile) in directions) {
            var rank = piece.position.rank + dRank
            var file = piece.position.file + dFile
            while (inBounds(Position(rank, file), board)) {
                val target = Position(rank, file)
                val occupant = board.grid[rank][file]?.pieceOn
                if (occupant != null) {
                    if (occupant.color != piece.color) moves.add(target)
                    break
                }
                moves.add(target)
                rank += dRank
                file += dFile
            }
        }
        return moves
    }

    private fun isInCheck(king: King, board: Board): Boolean {
        val (rank, file) = king.position
        val enemyColor = if (king.color == PieceColor.WHITE) PieceColor.BLACK else PieceColor.WHITE

        // check for enemy rooks/queens on rook lines
        rookMoves(king, board).forEach { move ->
            val piece = board.grid[move.rank][move.file]?.pieceOn
            if (piece?.color == enemyColor && (piece is Rook || piece is Queen)) return true
        }

        // check for enemy bishops/queens on diagonals
        bishopMoves(king, board).forEach { move ->
            val piece = board.grid[move.rank][move.file]?.pieceOn
            if (piece?.color == enemyColor && (piece is Bishop || piece is Queen)) return true
        }

        // check for enemy knights
        knightMoves(king, board).forEach { move ->
            val piece = board.grid[move.rank][move.file]?.pieceOn
            if (piece?.color == enemyColor && piece is Knight) return true
        }

        // check for enemy pawns
        val pawnRank = if (king.color == PieceColor.WHITE) rank + 1 else rank - 1
        for (dFile in listOf(-1, 1)) {
            val diag = Position(pawnRank, file + dFile)
            val piece = board.grid[diag.rank][diag.file]?.pieceOn
            if (inBounds(diag, board) && piece?.color == enemyColor && piece is Pawn) return true
        }

        return false
    }

    private fun leavesKingInCheck(piece: Piece, board: Board): Boolean {
        val cloneBoard = board.clone()
        cloneBoard.grid[piece.position.rank][piece.position.file]?.pieceOn = null
        val king = findKing(piece.color, cloneBoard) ?: return false
        return isInCheck(king, cloneBoard)
    }

    private fun isAdjacent(a: Position, b: Position): Boolean {
        return abs(a.rank - b.rank) <= 1 && abs(a.file - b.file) <= 1
    }

    private fun findKing(color: PieceColor, board: Board): King? {
        val enemyColor = if (color == PieceColor.WHITE) PieceColor.BLACK else PieceColor.WHITE
        for (rank in 0 until board.height) {
            for (file in 0 until board.width) {
                val piece = board.grid[rank][file]?.pieceOn
                if (piece is King && piece.color == enemyColor) return piece
            }
        }
        return null
    }

    private fun inBounds(position: Position, board: Board): Boolean {
        return position.rank in 0 until board.height && position.file in 0 until board.width
    }

    private fun pieceAt(position: Position, board: Board): Boolean {
        return board.grid[position.rank][position.file]?.pieceOn != null
    }

    private fun friendlyAt(position: Position, color: PieceColor, board: Board): Boolean {
        return board.grid[position.rank][position.file]?.pieceOn?.color == color
    }

    private fun enemyAt(position: Position, color: PieceColor, board: Board): Boolean {
        return board.grid[position.rank][position.file]?.pieceOn?.color != color
                && pieceAt(position, board)
    }
}