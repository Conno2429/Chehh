package io.github.conno2429.chehh.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.conno2429.chehh.moves.MoveRecord
import io.github.conno2429.chehh.moves.toNotation

@Composable
fun MoveHistory(moves: List<MoveRecord>) {
    Column(
        modifier = Modifier
            .width(200.dp)
            .height(512.dp)
            .background(Color(0xFF2B2B2B), RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        // header
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            Text("#", modifier = Modifier.width(32.dp), color = Color.Gray, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text("White", modifier = Modifier.weight(1f), color = Color.Gray, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text("Black", modifier = Modifier.weight(1f), color = Color.Gray, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }

        // move rows — pair up white and black moves
        val pairedMoves = moves.chunked(2)
        LazyColumn {
            itemsIndexed(pairedMoves) { index, pair ->
                val bgColor = if (index % 2 == 0) Color(0xFF3A3A3A) else Color(0xFF2B2B2B)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(bgColor)
                        .padding(horizontal = 4.dp, vertical = 6.dp)
                ) {
                    Text(
                        "${index + 1}.",
                        modifier = Modifier.width(32.dp),
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                    Text(
                        toNotation(pair[0]),
                        modifier = Modifier.weight(1f),
                        color = Color.White,
                        fontSize = 13.sp
                    )
                    Text(
                        if (pair.size > 1) toNotation(pair[1]) else "",
                        modifier = Modifier.weight(1f),
                        color = Color(0xFFAAAAAA),
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}