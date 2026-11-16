package com.example.soalujian.soal1.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.soalujian.soal1.model.CardDto
import com.example.soalujian.soal1.model.displayImage
import com.example.soalujian.soal1.model.toUsd

/** Komponen card terpisah (wajib di soal). */
@Composable
fun PokemonCardItem(
    card: CardDto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(PokeColors.Surface)
            .clickable(onClick = onClick)
            .padding(10.dp)
    ) {
        CardImage(
            url = card.displayImage,
            contentDescription = card.name,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.72f)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = card.name ?: "Unknown",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "${card.setName ?: "-"} \u2022 #${card.cardNumber ?: "-"}",
            color = PokeColors.TextSecondary,
            fontSize = 11.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "\u25B2 ${card.prices?.market.toUsd()}",
                color = PokeColors.Green,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(PokeColors.Green.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Spacer(Modifier.size(6.dp))
            Text(
                text = card.rarity ?: "-",
                color = PokeColors.TextSecondary,
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f, fill = false)
                    .background(PokeColors.SurfaceHigh, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

/** Gambar kartu dengan state loading & error (kalau gagal get gambar kartu). */
@Composable
fun CardImage(
    url: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    if (url.isNullOrBlank()) {
        CardImageError(modifier)
        return
    }
    SubcomposeAsyncImage(
        model = url,
        contentDescription = contentDescription,
        contentScale = ContentScale.Fit,
        modifier = modifier,
        loading = {
            Box(
                Modifier.fillMaxSize().background(PokeColors.SurfaceHigh),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = PokeColors.Red,
                    strokeWidth = 2.dp
                )
            }
        },
        error = { CardImageError(Modifier.fillMaxSize()) }
    )
}

@Composable
private fun CardImageError(modifier: Modifier) {
    Box(
        modifier = modifier.background(PokeColors.SurfaceHigh),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Gagal memuat\nkartu",
            color = PokeColors.Red,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )
    }
}
