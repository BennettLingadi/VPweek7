package com.example.soalujian.soal1.service

import com.example.soalujian.soal1.model.CardResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface PokemonService {
    @GET("cards")
    suspend fun searchCards(
        @Query("search") search: String,
        @Query("limit") limit: Int
    ): CardResponse
}

object PokemonApi {
    const val API_KEY = "pokeprice_free_f93210a9eda6a3f1dc0f41353f764dcaa3b31f920ce36729"
    private const val BASE_URL = "https://www.pokemonpricetracker.com/api/v2/"

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $API_KEY")
                .build()
            chain.proceed(request)
        }
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val service: PokemonService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonService::class.java)
    }
}
