package com.example.soalujian.soal1.model

import java.util.Locale

data class CardResponse(
    val data: List<CardDto>? = null
)

data class CardDto(
    val tcgPlayerId: String? = null,
    val name: String? = null,
    val setName: String? = null,
    val cardNumber: String? = null,
    val rarity: String? = null,
    val cardType: String? = null,
    val artist: String? = null,
    val imageCdnUrl: String? = null,
    val imageCdnUrl400: String? = null,
    val imageCdnUrl800: String? = null,
    val prices: PricesDto? = null
)

data class PricesDto(
    val market: Double? = null,
    val low: Double? = null,
    val sellers: Int? = null,
    val listings: Int? = null,
    val primaryPrinting: String? = null
)

val CardDto.displayImage: String?
    get() = imageCdnUrl400 ?: imageCdnUrl800 ?: imageCdnUrl

val CardDto.listKey: String
    get() = tcgPlayerId ?: "${name}-${setName}-${cardNumber}"

fun Double?.toUsd(): String =
    if (this == null) "-" else "\$" + String.format(Locale.US, "%.2f", this)
