package com.example.soalujian.soal1.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.soalujian.common.AppSearchField
import com.example.soalujian.common.ErrorView
import com.example.soalujian.common.UiState
import com.example.soalujian.soal1.model.CardDto
import com.example.soalujian.soal1.model.listKey
import com.example.soalujian.soal1.viewmodel.PokemonViewModel

object PokeColors {
    val Bg = Color(0xFF0E1216)
    val Surface = Color(0xFF161D26)
    val SurfaceHigh = Color(0xFF1F2833)
    val Red = Color(0xFFE03131)
    val Green = Color(0xFF1FBF84)
    val Yellow = Color(0xFFF2C230)
    val TextSecondary = Color(0xFF8C97A5)
}

private val suggestions = listOf("Charizard", "Pikachu", "Mewtwo", "Gengar", "Eevee", "Lugia")

@Composable
fun PokemonApp(viewModel: PokemonViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = PokeColors.Red,
            background = PokeColors.Bg,
            surface = PokeColors.Surface
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = PokeColors.Bg,
            contentColor = Color.White
        ) {
            Column(Modifier.systemBarsPadding()) {
                PokeHeader()

                AppSearchField(
                    value = viewModel.query,
                    onValueChange = viewModel::onQueryChange,
                    onSearch = { viewModel.search() },
                    onClear = viewModel::clearQuery,
                    placeholder = "Cari kartu Pokemon...",
                    accent = PokeColors.Red,
                    container = PokeColors.Surface,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(suggestions) { name ->
                        Text(
                            text = name,
                            color = Color.White,
                            fontSize = 13.sp,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(PokeColors.Surface)
                                .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(20.dp))
                                .clickable { viewModel.search(name) }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }

                when (val s = state) {
                    is UiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                        CircularProgressIndicator(color = PokeColors.Red)
                    }

                    is UiState.Error -> ErrorView(
                        title = "Connection Failed",
                        message = s.message,
                        buttonText = "Try Again",
                        accent = PokeColors.Red,
                        onRetry = { viewModel.search() },
                        modifier = Modifier.fillMaxSize()
                    )

                    is UiState.Success -> {
                        if (s.data.isEmpty()) {
                            Box(Modifier.fillMaxSize(), Alignment.Center) {
                                Text("Kartu tidak ditemukan", color = PokeColors.TextSecondary)
                            }
                        } else {
                            CardGrid(
                                cards = s.data,
                                onCardClick = viewModel::selectCard
                            )
                        }
                    }
                }
            }
        }

        viewModel.selectedCard?.let { card ->
            PokemonDetailSheet(card = card, onDismiss = viewModel::dismissDetail)
        }
    }
}

@Composable
private fun CardGrid(cards: List<CardDto>, onCardClick: (CardDto) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Market Results (${cards.size})",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "USD \u2022 Near Mint",
                    color = PokeColors.TextSecondary,
                    fontSize = 11.sp
                )
            }
        }
        itemsIndexed(cards, key = { index, card -> "$index-${card.listKey}" }) { _, card ->
            PokemonCardItem(card = card, onClick = { onCardClick(card) })
        }
    }
}

@Composable
private fun PokeHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PokeColors.Surface)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo pokeball sederhana (tanpa icon)
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(PokeColors.Red, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(Modifier.size(6.dp).background(Color.White, CircleShape))
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text("Pok\u00e9Tracker", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Real-time TCG Card Valuation", color = PokeColors.TextSecondary, fontSize = 11.sp)
        }
        Text(
            text = "\u25CF LIVE API",
            color = PokeColors.Green,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(PokeColors.Green.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                .padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}
