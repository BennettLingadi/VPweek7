package com.example.soalujian.common

import com.google.gson.JsonParseException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

fun Throwable.toReadableMessage(): String = when (this) {
    is HttpException -> "HTTP ${code()}"
    is UnknownHostException -> "Tidak ada koneksi internet"
    is SocketTimeoutException -> "Koneksi timeout, coba lagi"
    is IOException -> "Koneksi gagal"
    is JsonParseException -> "Respons server tidak valid (kemungkinan limit request tercapai)"
    else -> message ?: "Terjadi kesalahan"
}
