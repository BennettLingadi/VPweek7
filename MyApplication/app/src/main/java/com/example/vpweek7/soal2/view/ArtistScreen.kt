package com.example.soalujian.soal2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.soalujian.common.AppSearchField
import com.example.soalujian.common.ErrorView
import com.example.soalujian.common.UiState
import com.example.soalujian.soal2.model.AlbumDto
import com.example.soalujian.soal2.model.ArtistDto
import com.example.soalujian.soal2.model.bannerImage

@Composable
fun ArtistScreen(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    artistState: UiState<ArtistDto>,
    albumsState: UiState<List<AlbumDto>>,
    gridState: LazyGridState,
    onAlbumClick: (AlbumDto) -> Unit
) {
    var bioExpanded by rememberSaveable { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().systemBarsPadding()) {
        // Header
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            EqualizerLogo()
            Spacer(Modifier.width(10.dp))
            Text("Music Artist", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        AppSearchField(
            value = query,
            onValueChange = onQueryChange,
            onSearch = onSearch,
            onClear = onClear,
            placeholder = "Cari artis...",
            accent = MusicColors.Green,
            container = MusicColors.Surface,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(Modifier.height(12.dp))

        when (artistState) {
            is UiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = MusicColors.Green)
                    Spacer(Modifier.height(12.dp))
                    Text("Mencari artis...", color = MusicColors.TextSecondary, fontSize = 13.sp)
                }
            }

            is UiState.Error -> ErrorView(
                title = "Terjadi Kesalahan",
                message = artistState.message,
                buttonText = "Coba Lagi",
                accent = MusicColors.Green,
                onRetry = onSearch,
                modifier = Modifier.fillMaxSize()
            )

            is UiState.Success -> {
                val artist = artistState.data
                LazyVerticalGrid(
                    state = gridState,
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item(span = { GridItemSpan(maxLineSpan) }) { ArtistBanner(artist) }

                    item(span = { GridItemSpan(maxLineSpan) }) {
                        AboutSection(
                            bio = artist.strBiographyEN,
                            expanded = bioExpanded,
                            onToggle = { bioExpanded = !bioExpanded }
                        )
                    }

                    item(span = { GridItemSpan(maxLineSpan) }) {
                        DiscographyHeader(
                            count = (albumsState as? UiState.Success)?.data?.size
                        )
                    }

                    when (albumsState) {
                        is UiState.Loading -> item(span = { GridItemSpan(maxLineSpan) }) {
                            Box(Modifier.fillMaxWidth().padding(32.dp), Alignment.Center) {
                                CircularProgressIndicator(color = MusicColors.Green)
                            }
                        }

                        is UiState.Error -> item(span = { GridItemSpan(maxLineSpan) }) {
                            ErrorView(
                                title = "Gagal memuat album",
                                message = albumsState.message,
                                buttonText = "Coba Lagi",
                                accent = MusicColors.Green,
                                onRetry = onSearch
                            )
                        }

                        is UiState.Success -> {
                            if (albumsState.data.isEmpty()) {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    Text(
                                        "Belum ada album untuk artis ini",
                                        color = MusicColors.TextSecondary,
                                        fontSize = 13.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth().padding(24.dp)
                                    )
                                }
                            } else {
                                itemsIndexed(
                                    albumsState.data,
                                    key = { index, album -> "$index-${album.idAlbum}" }
                                ) { _, album ->
                                    AlbumItem(album = album, onClick = { onAlbumClick(album) })
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ArtistBanner(artist: ArtistDto) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MusicColors.SurfaceHigh)
    ) {
        AsyncImage(
            model = artist.bannerImage,
            contentDescription = artist.strArtist,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.4f to Color.Transparent,
                        1f to Color(0xEE0F1014)
                    )
                )
        )
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = artist.strArtistThumb,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MusicColors.SurfaceHigh)
                    .border(2.dp, Color(0x55FFFFFF), CircleShape)
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = artist.strArtist ?: "-",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = artist.strGenre ?: artist.strStyle ?: "-",
                        color = MusicColors.Green,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "  \u2022  ${artist.strCountry ?: "-"}",
                        color = Color(0xFFD0D4DA),
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun AboutSection(bio: String?, expanded: Boolean, onToggle: () -> Unit) {
    val text = bio?.trim().orEmpty()
    Column(Modifier.fillMaxWidth()) {
        Text("Tentang Artis", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))
        Text(
            text = text.ifEmpty { "Biografi belum tersedia." },
            color = MusicColors.TextSecondary,
            fontSize = 13.sp,
            lineHeight = 19.sp,
            maxLines = if (expanded) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis
        )
        if (text.length > 150) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = if (expanded) "Tutup ringkasan" else "Baca selengkapnya",
                color = MusicColors.Green,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable(onClick = onToggle)
            )
        }
    }
}

@Composable
private fun DiscographyHeader(count: Int?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("Diskografi Album", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Koleksi rilisan resmi", color = MusicColors.TextSecondary, fontSize = 11.sp)
        }
        if (count != null) {
            Text(
                text = "$count Album",
                color = MusicColors.Green,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(MusicColors.Green.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            )
        }
    }
}

@Composable
private fun AlbumItem(album: AlbumDto, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = album.strAlbumThumb,
            contentDescription = album.strAlbum,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(14.dp))
                .background(MusicColors.SurfaceHigh)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = album.strAlbum ?: "-",
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(2.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = album.intYearReleased ?: "-",
                color = MusicColors.Green,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = album.strGenre ?: "",
                color = MusicColors.TextSecondary,
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

/** Logo equalizer sederhana (tanpa icon). */
@Composable
private fun EqualizerLogo() {
    Box(
        modifier = Modifier
            .size(34.dp)
            .background(MusicColors.Green, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf(8, 14, 10, 16, 8).forEach { h ->
                Box(
                    Modifier
                        .width(2.dp)
                        .height(h.dp)
                        .background(Color(0xFF0F1014), RoundedCornerShape(1.dp))
                )
            }
        }
    }
}
