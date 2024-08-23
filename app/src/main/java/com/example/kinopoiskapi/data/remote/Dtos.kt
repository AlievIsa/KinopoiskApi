package com.example.kinopoiskapi.data.remote

data class MoviesResponse(
    val docs: List<MovieDto>,
    val total: Int,
    val limit: Int,
    val page: Int,
    val pages: Int
)

data class MovieDto(
    val id: Int,
    val name: String?,
    val type: String?,
    val typeNumber: Int?,
    val description: String?,
    val shortDescription: String?,
    val year: Int?,
    val ageRating: Int?,
    val movieLength: Int?,
    val logo: ImageDto?,
    val poster: ImageDto?,
    val backdrop: ImageDto?,
    val isSeries: Boolean?,
    val rating: RatingDto?,
    val votes: VotesDto?,
    val genres: List<GenreDto>?,
    val countries: List<CountryDto>?
) {
    val firstGenre: String?
        get() = genres?.firstOrNull()?.name

    val secondGenre: String?
        get() = if (genres != null && genres.size > 1) genres.elementAt(1).name else null

    val firstCountry: String?
        get() = countries?.firstOrNull()?.name
}

data class ImageDto(
    val url: String?,
    val previewUrl: String?
)

data class RatingDto(
    val kp: Double?,
    val imdb: Double?,
    val filmCritics: Double?,
    val russianFilmCritics: Double?,
    val await: Double?
)

data class VotesDto(
    val kp: Int?,
    val imdb: Int?,
    val filmCritics: Int?,
    val russianFilmCritics: Int?,
    val await: Int?
)

data class GenreDto(
    val name: String?
)

data class CountryDto(
    val name: String?
)

data class SeasonResponse(
    val docs: List<Any>,
    val total: Int,
    val limit: Int,
    val page: Int,
    val pages: Int
)
