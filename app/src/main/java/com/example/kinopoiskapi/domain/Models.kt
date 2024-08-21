package com.example.kinopoiskapi.domain

data class Movie(
    val id: Int = 0,
    val dtoId: Int = 0,
    val name: String? = null,
    val type: String? = null,
    val typeNumber: Int? = null,
    val description: String? = null,
    val shortDescription: String? = null,
    val year: Int? = null,
    val ageRating: Int? = null,
    val movieLength: Int?= null,
    val logo: Image? = null,
    val poster: Image? = null,
    val backdrop: Image? = null,
    val isSeries: Boolean? = null,
    val rating: Rating? = null,
    val votes: Votes? = null,
    val genre: String? = null,
    val country: String? = null
)

data class Image(
    val url: String? = null,
    val previewUrl: String? = null
)

data class Rating(
    val kp: Double? = null,
    val imdb: Double? = null,
    val filmCritics: Double? = null,
    val russianFilmCritics: Double? = null,
    val await: Double? = null
)

data class Votes(
    val kp: Int? = null,
    val imdb: Int? = null,
    val filmCritics: Int? = null,
    val russianFilmCritics: Int? = null,
    val await: Int? = null
)

data class SearchQuery(
    val id: Int,
    val text: String
)

data class Genre(
    val id: Int,
    val name: String?
)
