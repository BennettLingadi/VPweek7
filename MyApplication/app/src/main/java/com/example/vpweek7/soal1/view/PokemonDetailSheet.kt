package com.example.soalujian.soal1.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soalujian.soal1.model.CardDto
import com.example.soalujian.soal1.model.displayImage
import com.example.soalujian.soal1.model.toUsd

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailSheet(
    card: CardDto,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = PokeColors.Bg,
        contentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
        ) {
            // Header gradient + gambar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(listOf(Color(0xFF5A2A30), PokeColors.Bg))
                    )
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                CardImage(
                    url = card.displayImage,
                    contentDescription = card.name,
                    modifier = Modifier
                        .width(200.dp)
                        .height(280.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            }

            Column(Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = card.name ?: "Unknown",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = card.setName ?: "-",
                    color = PokeColors.TextSecondary,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    PriceBox(
                        label = "Market Price",
                        value = card.prices?.market.toUsd(),
                        color = PokeColors.Green,
                        modifier = Modifier.weight(1f)
                    )
                    PriceBox(
                        label = "Lowest Price",
                        value = card.prices?.low.toUsd(),
                        color = PokeColors.Yellow,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(20.dp))
                SectionTitle("Market Activity")
                InfoGroup(
                    listOf(
                        "Active Listings" to (card.prices?.listings?.toString() ?: "-"),
                        "Verified Sellers" to (card.prices?.sellers?.toString() ?: "-"),
                        "Primary Printing" to (card.prices?.primaryPrinting ?: "-")
                    )
                )

                Spacer(Modifier.height(20.dp))
                SectionTitle("Card Specifications")
                InfoGroup(
                    listOf(
                        "Card Number" to (card.cardNumber ?: "-"),
                        "Rarity" to (card.rarity ?: "-"),
                        "Type" to (card.cardType ?: "-"),
                        "Illustrator" to (card.artist ?: "-")
                    )
                )

                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PokeColors.Red,
                        contentColor = Color.White
                    )
                ) {
                    Text("Done", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun PriceBox(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(color.copy(alpha = 0.08f))
            .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Text(label, color = PokeColors.TextSecondary, fontSize = 12.sp)
        Spacer(Modifier.height(6.dp))
        Text(value, color = color, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = Color.White,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun InfoGroup(rows: List<Pair<String, String>>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(PokeColors.Surface)
            .padding(horizontal = 14.dp, vertical = 4.dp)
    ) {
        rows.forEach { (label, value) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(label, color = PokeColors.TextSecondary, fontSize = 13.sp)
                Text(
                    text = value,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.End,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}
