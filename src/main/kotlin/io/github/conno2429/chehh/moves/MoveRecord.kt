package io.github.conno2429.chehh.moves

import io.github.conno2429.chehh.pieces.Piece
import io.github.conno2429.chehh.pieces.Position
import kotlin.reflect.KClass

data class MoveRecord(
    val piece: Piece,
    val from: Position,
    val to: Position,
    val isCapture: Boolean = false,
    val isCheck: Boolean = false,
    val isCheckmate: Boolean = false,
    val disambigFile: Boolean = false,
    val disambigRank: Boolean = false,
    val promotionPiece: KClass<out Piece>? = null,
    val isCastle: Boolean = false,
    val isKingsideCastle: Boolean = false
)