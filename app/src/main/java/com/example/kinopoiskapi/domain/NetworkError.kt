package com.example.kinopoiskapi.domain

sealed class NetworkError(message: String): Exception(message) {
    class NoInternetConnection: NetworkError("No internet connection")
    class HttpError(val code: Int, message: String): NetworkError("Error $code: $message")
    class UnknownError(message: String): NetworkError(message)
}