package com.example.soalujian.soal2.model

import java.util.Locale

data class ArtistResponse(val artists: List<ArtistDto>? = null)

data class ArtistDto(
    val idArtist: String? = null,
    val strArtist: String? = null,
    val strGenre: String? = null,
    val strStyle: String? = null,
    val strCountry: String? = null,
    val strBiographyEN: String? = null,
    val strArtistThumb: String? = null,
    val strArtistBanner: String? = null,
    val strArtistFanart: String? = null
)

data class AlbumResponse(val album: List<AlbumDto>? = null)

data class AlbumDto(
    val idAlbum: String? = null,
    val strAlbum: String? = null,
    val strArtist: String? = null,
    val intYearReleased: String? = null,
    val strGenre: String? = null,
    val strStyle: String? = null,
    val strAlbumThumb: String? = null,
    val strDescriptionEN: String? = null
)

data class TrackResponse(val track: List<TrackDto>? = null)

data class TrackDto(
    val idTrack: String? = null,
    val strTrack: String? = null,
    val intTrackNumber: String? = null,
    val intDuration: String? = null
)

/** Gabungan detail album + daftar lagu untuk halaman detail. */
data class AlbumDetail(
    val album: AlbumDto,
    val tracks: List<TrackDto>
)

val ArtistDto.bannerImage: String?
    get() = strArtistBanner?.takeIf { it.isNotBlank() }
        ?: strArtistFanart?.takeIf { it.isNotBlank() }
        ?: strArtistThumb?.takeIf { it.isNotBlank() }

fun TrackDto.durationText(): String {
    val ms = intDuration?.toLongOrNull() ?: return "--:--"
    val totalSeconds = ms / 1000
    return String.format(Locale.US, "%d:%02d", totalSeconds / 60, totalSeconds % 60)
}
