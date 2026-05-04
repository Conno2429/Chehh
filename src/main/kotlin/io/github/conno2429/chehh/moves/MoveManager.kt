package io.github.conno2429.chehh.moves

import io.github.conno2429.chehh.GameManager
import io.github.conno2429.chehh.board.Board
import io.github.conno2429.chehh.board.clone
import io.github.conno2429.chehh.pieces.Bishop
import io.github.conno2429.chehh.pieces.King
import io.github.conno2429.chehh.pieces.Knight
import io.github.conno2429.chehh.pieces.Pawn
import io.github.conno2429.chehh.pieces.Piece
import io.github.conno2429.chehh.pieces.PieceColor
import io.github.conno2429.chehh.pieces.PlaceHolder
import io.github.conno2429.chehh.pieces.Position
import io.github.conno2429.chehh.pieces.Queen
import io.github.conno2429.chehh.pieces.Rook
import kotlin.math.abs

object MoveManager {
    fun isLegalMove(piece: Piece, target: Position, board: Board): Boolean {
        return true
    }

    fun getValidMoves(piece: Piece, board: Board): List<Position> {
        return generateMoves(piece, board)
    }

    private fun generateMoves(piece: Piece, board: Board): List<Position> {
        val raw = when (piece) {
            is Pawn -> pawnMoves(piece, board)
            is Rook -> rookMoves(piece, board)
            is Knight -> knightMoves(piece, board)
            is Bishop -> bishopMoves(piece, board)
            is Queen -> queenMoves(piece, board)
            is King -> kingMoves(piece, board)
            is PlaceHolder -> listOf()
        }

        return raw.filter { !leavesKingInCheck(piece, it, board) }
    }

    private fun pawnMoves(piece: Piece, board: Board): List<Position> {
        val moves = mutableListOf<Position>()
        val position = piece.position
        val last = GameManager.lastMove

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
                    if (last != null && last.piece is Pawn &&
                        abs(last.from.rank - last.to.rank) == 2 &&
                        abs(last.to.file - position.file) == 1 &&
                        last.to.rank == position.rank) {
                        // add the en passant capture square
                        moves.add(Position(position.rank + 1, last.to.file))
                    }
                }
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
                    if (last != null && last.piece is Pawn &&
                        abs(last.from.rank - last.to.rank) == 2 &&
                        abs(last.to.file - position.file) == 1 &&
                        last.to.rank == position.rank) {
                        // add the en passant capture square
                        moves.add(Position(position.rank - 1, last.to.file))
                    }
                }
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

    fun kingMoves(piece: King, board: Board): List<Position> {
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

        val enemyColor = if (piece.color == PieceColor.WHITE) PieceColor.BLACK else PieceColor.WHITE
        val enemyKingPos = findKing(enemyColor, board)?.position

        candidates.forEach {
            if (!inBounds(it, board)) println("$it filtered: out of bounds")
            else if (friendlyAt(it, piece.color, board)) println("$it filtered: friendly piece")
            else if (enemyKingPos != null && isAdjacent(it, enemyKingPos)) println("$it filtered: adjacent to enemy king")
            else if (leavesKingInCheck(piece, it, board)) println("$it filtered: leaves king in check")
            else println("$it ALLOWED")
        }

        return candidates.filter {
            inBounds(it, board)
                    && !friendlyAt(it, piece.color, board)
                    && (enemyKingPos == null || !isAdjacent(it, enemyKingPos))
                    && !leavesKingInCheck(piece, it, board)
        } + addCastle(piece, board)
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

    private fun addCastle(king: King, board: Board): List<Position> {
        val castleMoves = mutableListOf<Position>()
        if (king.hasMoved || isInCheck(king, board)) return castleMoves

        // kingside
        val kingsideRook = findCastleRook(king, true, board)
        if (kingsideRook != null && !kingsideRook.hasMoved) {
            if (castlePathClear(king, kingsideRook, board) &&
                castlePathSafe(king, true, board)) {
                castleMoves.add(Position(king.position.rank, 6))
            }
        }

        // queenside
        val queensideRook = findCastleRook(king, false, board)
        if (queensideRook != null && !queensideRook.hasMoved) {
            if (castlePathClear(king, queensideRook, board) &&
                castlePathSafe(king, false, board)) {
                castleMoves.add(Position(king.position.rank, 2))
            }
        }

        return castleMoves
    }

    private fun castlePathClear(king: King, rook: Rook, board: Board): Boolean {
        val rank = king.position.rank
        val from = minOf(king.position.file, rook.position.file) + 1
        val to = maxOf(king.position.file, rook.position.file)
        for (file in from until to) {
            val occupant = board.grid[rank][file]?.pieceOn
            if (occupant != null && occupant != king && occupant != rook) return false
        }
        return true
    }

    private fun castlePathSafe(king: King, kingSide: Boolean, board: Board): Boolean {
        val rank = king.position.rank
        val files = if (kingSide) king.position.file..6 else 2..king.position.file
        for (file in files) {
            val placeholder = PlaceHolder(king.color, Position(rank, file))
            if (isInCheck(placeholder, board)) return false
        }
        return true
    }

    private fun isInCheck(king: Piece, board: Board): Boolean {
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
            if (!inBounds(diag, board)) continue
            val piece = board.grid[diag.rank][diag.file]?.pieceOn
            if (piece?.color == enemyColor && piece is Pawn) return true
        }

        return false
    }

    fun isInCheck(color: PieceColor, board: Board): Boolean {
        val king = findKing(color, board) ?: return false
        return isInCheck(king, board)
    }

    fun isCheckmate(color: PieceColor, board: Board): Boolean {
        if (!isInCheck(color, board)) return false
        for (rank in 0 until board.height) {
            for (file in 0 until board.width) {
                val piece = board.grid[rank][file]?.pieceOn ?: continue
                if (piece.color != color) continue
                val moves = generateMoves(piece, board)
                println("$piece at ${piece.position} has ${moves.size} moves: $moves")
                if (moves.isNotEmpty()) return false
            }
        }
        return true
    }

    fun isStalemate(color: PieceColor, board: Board): Boolean {
        if (isInCheck(color, board)) return false
        for (rank in 0 until board.height) {
            for (file in 0 until board.width) {
                val piece = board.grid[rank][file]?.pieceOn ?: continue
                if (piece.color != color) continue
                if (generateMoves(piece, board).isNotEmpty()) return false
            }
        }
        return true
    }

    private fun leavesKingInCheck(piece: Piece, target: Position, board: Board): Boolean {
        val simBoard = board.clone()
        val simPiece = simBoard.grid[piece.position.rank][piece.position.file]?.pieceOn ?: return false
        simBoard.grid[piece.position.rank][piece.position.file]?.pieceOn = null
        simBoard.grid[target.rank][target.file]?.pieceOn = simPiece
        simPiece.position = target

        val king = if (piece is King) {
            simBoard.grid[target.rank][target.file]?.pieceOn as? King ?: return true
        } else {
            findKing(piece.color, simBoard) ?: return true
        }

        return isInCheck(king, simBoard)
    }

    private fun isAdjacent(a: Position, b: Position): Boolean {
        return abs(a.rank - b.rank) <= 1 && abs(a.file - b.file) <= 1
    }

    private fun findKing(color: PieceColor, board: Board): King? {
        for (rank in 0 until board.height) {
            for (file in 0 until board.width) {
                val piece = board.grid[rank][file]?.pieceOn
                if (piece is King && piece.color == color) return piece
            }
        }
        return null
    }

    private fun findCastleRook(king: Piece, kingSide: Boolean, board: Board): Rook? {
        val rank = king.position.rank
        val range = if (kingSide) (king.position.file + 1) until board.width
        else 0 until king.position.file
        for (file in range) {
            val piece = board.grid[rank][file]?.pieceOn
            if (piece is Rook && piece.color == king.color) return piece
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

    fun allLegalMoves(color: PieceColor, board: Board): Map<Piece, List<Position>> {
        val result = mutableMapOf<Piece, List<Position>>()
        for (rank in 0 until board.height) {
            for (file in 0 until board.width) {
                val piece = board.grid[rank][file]?.pieceOn ?: continue
                if (piece.color != color) continue
                val moves = generateMoves(piece, board)
                if (moves.isNotEmpty()) result[piece] = moves
            }
        }
        return result
    }
}