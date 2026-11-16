package com.example.soalujian.soal2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import coil.compose.AsyncImage
import com.example.soalujian.common.ErrorView
import com.example.soalujian.common.UiState
import com.example.soalujian.soal2.model.AlbumDetail
import com.example.soalujian.soal2.model.AlbumDto
import com.example.soalujian.soal2.model.durationText

@Composable
fun AlbumDetailScreen(
    album: AlbumDto,
    state: UiState<AlbumDetail>,
    onBack: () -> Unit,
    onRetry: () -> Unit
) {
    Column(Modifier.fillMaxSize().systemBarsPadding()) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center
            ) {
                Text("\u2190", color = Color.White, fontSize = 22.sp)
            }
            Spacer(Modifier.width(8.dp))
            Text("Detail Album", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        when (state) {
            is UiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = MusicColors.Green)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Memuat detail album & track list...",
                        color = MusicColors.TextSecondary,
                        fontSize = 13.sp
                    )
                }
            }

            is UiState.Error -> ErrorView(
                title = "Gagal memuat detail album",
                message = state.message,
                buttonText = "Coba Lagi",
                accent = MusicColors.Green,
                onRetry = onRetry,
                modifier = Modifier.fillMaxSize()
            )

            is UiState.Success -> DetailContent(state.data, fallbackName = album.strAlbum)
        }
    }
}

@Composable
private fun DetailContent(detail: AlbumDetail, fallbackName: String?) {
    val album = detail.album
    var descExpanded by remember { mutableStateOf(false) }
    val description = album.strDescriptionEN?.trim().orEmpty()

    LazyColumn(
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(
                    model = album.strAlbumThumb,
                    contentDescription = album.strAlbum,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MusicColors.SurfaceHigh)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = album.strAlbum ?: fallbackName ?: "-",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = album.strArtist ?: "",
                    color = MusicColors.TextSecondary,
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Chip(album.intYearReleased ?: "-", highlight = true)
                    Chip(album.strGenre ?: album.strStyle ?: "-", highlight = false)
                }
                Spacer(Modifier.height(16.dp))
            }
        }

        item {
            Column(Modifier.fillMaxWidth()) {
                Text("Deskripsi", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Text(
                    text = description.ifEmpty { "Deskripsi belum tersedia." },
                    color = MusicColors.TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    maxLines = if (descExpanded) Int.MAX_VALUE else 4,
                    overflow = TextOverflow.Ellipsis
                )
                if (description.length > 200) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = if (descExpanded) "Tutup ringkasan" else "Baca selengkapnya",
                        color = MusicColors.Green,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { descExpanded = !descExpanded }
                    )
                }
                Spacer(Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Daftar Lagu", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(
                        "${detail.tracks.size} lagu",
                        color = MusicColors.TextSecondary,
                        fontSize = 12.sp
                    )
                }
                Spacer(Modifier.height(6.dp))
            }
        }

        if (detail.tracks.isEmpty()) {
            item {
                Text(
                    "Daftar lagu tidak tersedia",
                    color = MusicColors.TextSecondary,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(24.dp)
                )
            }
        } else {
            itemsIndexed(
                detail.tracks,
                key = { index, track -> "$index-${track.idTrack}" }
            ) { index, track ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MusicColors.Surface)
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = track.intTrackNumber?.takeIf { it.isNotBlank() && it != "0" }
                            ?: (index + 1).toString(),
                        color = MusicColors.Green,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(28.dp)
                    )
                    Text(
                        text = track.strTrack ?: "-",
                        color = Color.White,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = track.durationText(),
                        color = MusicColors.TextSecondary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun Chip(text: String, highlight: Boolean) {
    Text(
        text = text,
        color = if (highlight) MusicColors.Green else Color(0xFFD0D4DA),
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .background(
                if (highlight) MusicColors.Green.copy(alpha = 0.15f) else MusicColors.SurfaceHigh,
                RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 5.dp)
    )
}
