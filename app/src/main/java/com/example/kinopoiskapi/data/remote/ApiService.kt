package com.example.kinopoiskapi.data.remote

interface ApiService {

    suspend fun getMoviesResponse(
        query: String,
        page: Int,
        limit: Int,
        type: Int,
        year: String?,
        rateRange: IntRange,
        genre: String?
    ): MoviesResponse

    suspend fun getGenres(): List<GenreDto>
}


