package com.example.kinopoiskapi.data.mappers


import com.example.kinopoiskapi.data.local.GenreEntity
import com.example.kinopoiskapi.data.local.ImageEntity
import com.example.kinopoiskapi.data.local.MovieEntity
import com.example.kinopoiskapi.data.local.RatingEntity
import com.example.kinopoiskapi.data.local.SearchQueryEntity
import com.example.kinopoiskapi.data.local.VotesEntity
import com.example.kinopoiskapi.data.remote.GenreDto
import com.example.kinopoiskapi.data.remote.ImageDto
import com.example.kinopoiskapi.data.remote.MovieDto
import com.example.kinopoiskapi.data.remote.RatingDto
import com.example.kinopoiskapi.data.remote.VotesDto
import com.example.kinopoiskapi.domain.Genre
import com.example.kinopoiskapi.domain.Image
import com.example.kinopoiskapi.domain.Movie
import com.example.kinopoiskapi.domain.Rating
import com.example.kinopoiskapi.domain.SearchQuery
import com.example.kinopoiskapi.domain.Votes

fun MovieDto.toMovieEntity(query: String): MovieEntity {
    return MovieEntity(
        dtoId = id,
        query = query,
        name = name,
        type = type,
        typeNumber = typeNumber,
        description = description,
        shortDescription = shortDescription,
        year = year,
        ageRating = ageRating,
        movieLength = movieLength,
        logo = logo?.toImageEntity(),
        poster = poster?.toImageEntity(),
        backdrop = backdrop?.toImageEntity(),
        isSeries = isSeries,
        rating = rating?.toRatingEntity(),
        votes = votes?.toVotesEntity(),
        genre = firstGenre,
        country = firstCountry
    )
}

fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = id,
        dtoId = dtoId,
        name = name,
        type = type,
        typeNumber = typeNumber,
        description = description,
        shortDescription = shortDescription,
        year = year,
        ageRating = ageRating,
        movieLength = movieLength,
        logo = logo?.toImage(),
        poster = poster?.toImage(),
        backdrop = backdrop?.toImage(),
        isSeries = isSeries,
        rating = rating?.toRating(),
        votes = votes?.toVotes(),
        genre = genre,
        country = country
    )
}

fun ImageDto.toImageEntity(): ImageEntity {
    return ImageEntity(
        url = url,
        previewUrl = previewUrl
    )
}

fun ImageEntity.toImage(): Image {
    return Image(
        url = url,
        previewUrl = previewUrl
    )
}

fun RatingDto.toRatingEntity(): RatingEntity {
    return RatingEntity(
        kp = kp,
        imdb = imdb,
        filmCritics = filmCritics,
        russianFilmCritics = russianFilmCritics,
        await = await
    )
}

fun RatingEntity.toRating(): Rating {
    return Rating(
        kp = kp,
        imdb = imdb,
        filmCritics = filmCritics,
        russianFilmCritics = russianFilmCritics,
        await = await
    )
}

fun VotesDto.toVotesEntity(): VotesEntity {
    return VotesEntity(
        kp = kp,
        imdb = imdb,
        filmCritics = filmCritics,
        russianFilmCritics = russianFilmCritics,
        await = await
    )
}

fun VotesEntity.toVotes(): Votes {
    return Votes(
        kp = kp,
        imdb = imdb,
        filmCritics = filmCritics,
        russianFilmCritics = russianFilmCritics,
        await = await
    )
}

fun SearchQueryEntity.toSearchQuery(): SearchQuery {
    return SearchQuery(
        id = id,
        text = text
    )
}

fun SearchQuery.toSearchQueryEntity(): SearchQueryEntity {
    return SearchQueryEntity(
        id = id,
        text = text
    )
}

fun GenreDto.toGenreEntity(): GenreEntity {
    return GenreEntity(
        name = name
    )
}

fun GenreEntity.toGenre(): Genre {
    return Genre(
        id = id,
        name = name
    )
}