package io.github.conno2429.chehh.moves

import io.github.conno2429.chehh.board.Board
import io.github.conno2429.chehh.pieces.Bishop
import io.github.conno2429.chehh.pieces.King
import io.github.conno2429.chehh.pieces.Knight
import io.github.conno2429.chehh.pieces.Pawn
import io.github.conno2429.chehh.pieces.Piece
import io.github.conno2429.chehh.pieces.PlaceHolder
import io.github.conno2429.chehh.pieces.Position
import io.github.conno2429.chehh.pieces.Queen
import io.github.conno2429.chehh.pieces.Rook
import kotlin.reflect.KClass

fun checkAmbiguous(piece: Piece, position: Position, board: Board) {
    val (oRank, oFile) = piece.position
    val (rank, file) = position

    val placeHolder = PlaceHolder(piece.color, position)

    when (piece) {
        is Pawn -> {

        }
        is Knight -> {

        }
        is Bishop -> {

        }
        is Rook -> {

        }
        is Queen -> {

        }
        else -> {
            
        }
    }
}

fun toNotation(move: MoveRecord): String {
    // castling
    if (move.isCastle) {
        val base = if (move.isKingsideCastle) "O-O" else "O-O-O"
        return base + checkSuffix(move)
    }

    val sb = StringBuilder()

    // piece prefix — pawns have no prefix
    val pieceChar = when (move.piece) {
        is Knight -> "N"
        is Bishop -> "B"
        is Rook -> "R"
        is Queen -> "Q"
        is King -> "K"
        else -> "" // pawn
    }
    sb.append(pieceChar)

    // disambiguation
    if (move.disambigFile) sb.append(fileToChar(move.from.file))
    if (move.disambigRank) sb.append(move.from.rank + 1)

    // pawn capture includes from file
    if (move.piece is Pawn && move.isCapture) sb.append(fileToChar(move.from.file))

    // capture
    if (move.isCapture) sb.append("x")

    // destination
    sb.append(fileToChar(move.to.file))
    sb.append(move.to.rank + 1)

    // promotion
    move.promotionPiece?.let { sb.append("=").append(pieceClassToChar(it)) }

    // check / checkmate
    sb.append(checkSuffix(move))

    return sb.toString()
}

private fun checkSuffix(move: MoveRecord) = when {
    move.isCheckmate -> "#"
    move.isCheck -> "+"
    else -> ""
}

private fun fileToChar(file: Int) = ('a' + file).toString()

private fun pieceClassToChar(klass: KClass<out Piece>) = when (klass) {
    Queen::class -> "Q"
    Rook::class -> "R"
    Bishop::class -> "B"
    Knight::class -> "N"
    else -> ""
}