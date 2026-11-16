package com.example.soalujian.soal2.repository

import com.example.soalujian.soal2.model.AlbumDetail
import com.example.soalujian.soal2.model.AlbumDto
import com.example.soalujian.soal2.model.ArtistDto
import com.example.soalujian.soal2.service.MusicApi
import com.example.soalujian.soal2.service.MusicService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class MusicRepository(
    private val service: MusicService = MusicApi.service
) {
    suspend fun searchArtist(name: String): ArtistDto? =
        service.searchArtist(name).artists?.firstOrNull()

    suspend fun searchAlbums(name: String): List<AlbumDto> =
        service.searchAlbums(name).album.orEmpty()

    /** Ambil detail album + track secara paralel. */
    suspend fun getAlbumDetail(fallback: AlbumDto): AlbumDetail = coroutineScope {
        val id = fallback.idAlbum.orEmpty()
        val albumDeferred = async { service.getAlbum(id).album?.firstOrNull() }
        val tracksDeferred = async { service.getTracks(id).track.orEmpty() }
        AlbumDetail(
            album = albumDeferred.await() ?: fallback,
            tracks = tracksDeferred.await()
        )
    }
}
