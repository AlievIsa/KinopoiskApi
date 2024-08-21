package com.example.kinopoiskapi.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dtoId: Int,
    val query: String,
    val name: String?,
    val type: String?,
    val typeNumber: Int?,
    val description: String?,
    val shortDescription: String?,
    val year: Int?,
    val ageRating: Int?,
    val movieLength: Int?,
    @Embedded(prefix = "logo_") val logo: ImageEntity?,
    @Embedded(prefix = "poster_") val poster: ImageEntity?,
    @Embedded(prefix = "backdrop_") val backdrop: ImageEntity?,
    val isSeries: Boolean?,
    @Embedded(prefix = "rating_") val rating: RatingEntity?,
    @Embedded(prefix = "votes_") val votes: VotesEntity?,
    val genre: String?,
    val country: String?
)

data class ImageEntity(
    val url: String?,
    val previewUrl: String?
)

data class RatingEntity(
    val kp: Double?,
    val imdb: Double?,
    val filmCritics: Double?,
    val russianFilmCritics: Double?,
    val await: Double?
)

data class VotesEntity(
    val kp: Int?,
    val imdb: Int?,
    val filmCritics: Int?,
    val russianFilmCritics: Int?,
    val await: Int?
)

@Entity
data class RemoteKey(
    @PrimaryKey
    val movieName: String,
    val nextKey: Int?
)

@Entity
data class SearchQueryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String
)

@Entity
data class GenreEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String?
)