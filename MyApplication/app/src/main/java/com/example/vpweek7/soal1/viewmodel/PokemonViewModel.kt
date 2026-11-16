package com.example.soalujian.soal1.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soalujian.common.UiState
import com.example.soalujian.common.toReadableMessage
import com.example.soalujian.soal1.model.CardDto
import com.example.soalujian.soal1.repository.PokemonRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonViewModel(
    private val repository: PokemonRepository = PokemonRepository()
) : ViewModel() {

    var query by mutableStateOf("Charizard")
        private set

    private val _state = MutableStateFlow<UiState<List<CardDto>>>(UiState.Loading)
    val state: StateFlow<UiState<List<CardDto>>> = _state.asStateFlow()

    var selectedCard by mutableStateOf<CardDto?>(null)
        private set

    private var searchJob: Job? = null

    init {
        search()
    }

    fun onQueryChange(value: String) {
        query = value
    }

    fun clearQuery() {
        query = ""
    }

    fun search(term: String = query) {
        val keyword = term.trim()
        if (keyword.isEmpty()) return
        query = keyword

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _state.value = UiState.Loading
            _state.value = try {
                UiState.Success(repository.searchCards(keyword))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                UiState.Error(e.toReadableMessage())
            }
        }
    }

    fun selectCard(card: CardDto) {
        selectedCard = card
    }

    fun dismissDetail() {
        selectedCard = null
    }
}
