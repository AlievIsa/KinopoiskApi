package com.example.kinopoiskapi.data.remote

interface ApiService {

    suspend fun getMoviesResponse(
        query: String,
        page: Int,
        limit: Int
    ): MoviesResponse
}


