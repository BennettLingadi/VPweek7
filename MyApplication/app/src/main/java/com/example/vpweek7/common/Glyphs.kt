package com.example.soalujian.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/** Glyph digambar manual lewat Canvas (tanpa icon library / drawable). */
@Composable
fun SearchGlyph(color: Color, modifier: Modifier = Modifier.size(20.dp)) {
    Canvas(modifier) {
        val stroke = size.minDimension * 0.11f
        drawCircle(
            color = color,
            radius = size.minDimension * 0.33f,
            center = Offset(size.width * 0.42f, size.height * 0.42f),
            style = Stroke(width = stroke)
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.66f, size.height * 0.66f),
            end = Offset(size.width * 0.92f, size.height * 0.92f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun CloseGlyph(color: Color, modifier: Modifier = Modifier.size(14.dp)) {
    Canvas(modifier) {
        val stroke = size.minDimension * 0.14f
        drawLine(color, Offset(0f, 0f), Offset(size.width, size.height), stroke, StrokeCap.Round)
        drawLine(color, Offset(size.width, 0f), Offset(0f, size.height), stroke, StrokeCap.Round)
    }
}
