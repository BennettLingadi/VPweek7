package com.example.soalujian.soal2.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.soalujian.soal2.viewmodel.MusicViewModel

object MusicColors {
    val Bg = Color(0xFF0F1014)
    val Surface = Color(0xFF191B21)
    val SurfaceHigh = Color(0xFF23262E)
    val Green = Color(0xFF1DB954)
    val TextSecondary = Color(0xFF9AA0AA)
}

@Composable
fun MusicApp(viewModel: MusicViewModel = viewModel()) {
    val artistState by viewModel.artistState.collectAsStateWithLifecycle()
    val albumsState by viewModel.albumsState.collectAsStateWithLifecycle()
    val detailState by viewModel.detailState.collectAsStateWithLifecycle()
    val gridState = rememberLazyGridState()
    val selected = viewModel.selectedAlbum

    BackHandler(enabled = selected != null) { viewModel.closeAlbum() }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = MusicColors.Green,
            background = MusicColors.Bg,
            surface = MusicColors.Surface
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MusicColors.Bg,
            contentColor = Color.White
        ) {
            Box(Modifier.fillMaxSize()) {
                // Halaman artis tetap di-compose supaya posisi scroll & state tidak hilang
                ArtistScreen(
                    query = viewModel.query,
                    onQueryChange = viewModel::onQueryChange,
                    onSearch = { viewModel.search() },
                    onClear = viewModel::clearQuery,
                    artistState = artistState,
                    albumsState = albumsState,
                    gridState = gridState,
                    onAlbumClick = viewModel::openAlbum
                )

                if (selected != null) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {},
                        color = MusicColors.Bg,
                        contentColor = Color.White
                    ) {
                        AlbumDetailScreen(
                            album = selected,
                            state = detailState,
                            onBack = viewModel::closeAlbum,
                            onRetry = viewModel::retryDetail
                        )
                    }
                }
            }
        }
    }
}
