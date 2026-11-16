package com.example.soalujian.soal1.repository

import com.example.soalujian.soal1.model.CardDto
import com.example.soalujian.soal1.service.PokemonApi
import com.example.soalujian.soal1.service.PokemonService

class PokemonRepository(
    private val service: PokemonService = PokemonApi.service
) {
    companion object {

        const val PAGE_LIMIT = 20
    }

    private val cache = mutableMapOf<String, List<CardDto>>()

    suspend fun searchCards(query: String): List<CardDto> {
        val key = query.trim().lowercase()
        cache[key]?.let { return it }

        val result = service.searchCards(query.trim(), PAGE_LIMIT).data.orEmpty()
        if (result.isNotEmpty()) cache[key] = result
        return result
    }
}
