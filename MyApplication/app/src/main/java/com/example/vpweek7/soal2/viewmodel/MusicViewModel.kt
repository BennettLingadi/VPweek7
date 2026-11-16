package com.example.soalujian.soal2.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soalujian.common.UiState
import com.example.soalujian.common.toReadableMessage
import com.example.soalujian.soal2.model.AlbumDetail
import com.example.soalujian.soal2.model.AlbumDto
import com.example.soalujian.soal2.model.ArtistDto
import com.example.soalujian.soal2.repository.MusicRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MusicViewModel(
    private val repository: MusicRepository = MusicRepository()
) : ViewModel() {

    var query by mutableStateOf("Coldplay")
        private set

    private val _artistState = MutableStateFlow<UiState<ArtistDto>>(UiState.Loading)
    val artistState: StateFlow<UiState<ArtistDto>> = _artistState.asStateFlow()

    private val _albumsState = MutableStateFlow<UiState<List<AlbumDto>>>(UiState.Loading)
    val albumsState: StateFlow<UiState<List<AlbumDto>>> = _albumsState.asStateFlow()

    private val _detailState = MutableStateFlow<UiState<AlbumDetail>>(UiState.Loading)
    val detailState: StateFlow<UiState<AlbumDetail>> = _detailState.asStateFlow()

    var selectedAlbum by mutableStateOf<AlbumDto?>(null)
        private set

    private var searchJob: Job? = null
    private var detailJob: Job? = null

    init {
        search()
    }

    fun onQueryChange(value: String) {
        query = value
    }

    fun clearQuery() {
        query = ""
    }

    fun search(name: String = query) {
        val term = name.trim()
        if (term.isEmpty()) return
        query = term

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _artistState.value = UiState.Loading
            _albumsState.value = UiState.Loading
            try {
                // Panggil 2 endpoint secara paralel dengan coroutine (async)
                val (artist, albums) = coroutineScope {
                    val artistDeferred = async { repository.searchArtist(term) }
                    val albumsDeferred = async { repository.searchAlbums(term) }
                    artistDeferred.await() to albumsDeferred.await()
                }
                if (artist == null) {
                    val msg = "Artis \"$term\" tidak ditemukan"
                    _artistState.value = UiState.Error(msg)
                    _albumsState.value = UiState.Error(msg)
                } else {
                    _artistState.value = UiState.Success(artist)
                    _albumsState.value = UiState.Success(albums)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                val msg = e.toReadableMessage()
                _artistState.value = UiState.Error(msg)
                _albumsState.value = UiState.Error(msg)
            }
        }
    }

    fun openAlbum(album: AlbumDto) {
        selectedAlbum = album
        loadDetail(album)
    }

    fun retryDetail() {
        selectedAlbum?.let { loadDetail(it) }
    }

    fun closeAlbum() {
        detailJob?.cancel()
        selectedAlbum = null
    }

    private fun loadDetail(album: AlbumDto) {
        detailJob?.cancel()
        detailJob = viewModelScope.launch {
            _detailState.value = UiState.Loading
            _detailState.value = try {
                UiState.Success(repository.getAlbumDetail(album))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                UiState.Error(e.toReadableMessage())
            }
        }
    }
}
