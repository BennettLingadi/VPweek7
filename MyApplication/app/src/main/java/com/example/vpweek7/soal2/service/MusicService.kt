package com.example.soalujian.soal2.service

import com.example.soalujian.soal2.model.AlbumResponse
import com.example.soalujian.soal2.model.ArtistResponse
import com.example.soalujian.soal2.model.TrackResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface MusicService {
    @GET("search.php")
    suspend fun searchArtist(@Query("s") artistName: String): ArtistResponse

    @GET("searchalbum.php")
    suspend fun searchAlbums(@Query("s") artistName: String): AlbumResponse

    @GET("album.php")
    suspend fun getAlbum(@Query("m") albumId: String): AlbumResponse

    @GET("track.php")
    suspend fun getTracks(@Query("m") albumId: String): TrackResponse
}

object MusicApi {
    // Base URL: https://www.theaudiodb.com/api/v1/json/123/(endpoint)
    private const val BASE_URL = "https://www.theaudiodb.com/api/v1/json/123/"

    private val client = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val service: MusicService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MusicService::class.java)
    }
}
