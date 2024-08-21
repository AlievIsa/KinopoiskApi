package com.example.kinopoiskapi.domain

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.kinopoiskapi.data.local.KinopoiskDatabase
import com.example.kinopoiskapi.data.local.SearchQueryEntity
import com.example.kinopoiskapi.data.mappers.toGenre
import com.example.kinopoiskapi.data.mappers.toGenreEntity
import com.example.kinopoiskapi.data.mappers.toMovie
import com.example.kinopoiskapi.data.mappers.toSearchQueryEntity
import com.example.kinopoiskapi.data.remote.ApiService
import com.example.kinopoiskapi.data.remote.MovieRemoteMediator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
class Repository(
    private val kinopoiskDb: KinopoiskDatabase,
    private val kinopoiskApi: ApiService
) {

    fun getMovieFlowPagingData(
        query: String,
        type: Int,
        year: String?,
        rate: IntRange,
        genre: String?
    ): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = MovieRemoteMediator(
                query = query,
                type = type,
                year = year,
                rate = rate,
                genre = genre,
                kinopoiskDb = kinopoiskDb,
                kinopoiskApi = kinopoiskApi
            ),
            pagingSourceFactory = {
                kinopoiskDb.movieDao.pagingSource(query)
            }
        ).flow.map { it.map { it.toMovie() } }
    }

    suspend fun getGenres(): List<Genre> {
        val genresFromDb = kinopoiskDb.genreDao.getGenres()
        if (genresFromDb.isNotEmpty()) {
            return genresFromDb.map { it.toGenre() }
        } else {
            val genresFromApi = kinopoiskApi.getGenres()
            kinopoiskDb.genreDao.upsertGenres(genresFromApi.map { it.toGenreEntity() })
        }
        return genresFromDb.map { it.toGenre() }
    }

    suspend fun getMovieById(id: Int) = kinopoiskDb.movieDao.getMovieById(id).toMovie()

    suspend fun upsertSearchQuery(query: String) = kinopoiskDb.searchQueryDao.upsert(SearchQueryEntity(text = query))

    fun getSearchQueries(query: String) = kinopoiskDb.searchQueryDao.getSearchQueries(query)

    suspend fun deleteSearchQuery(searchQuery: SearchQuery) {
        kinopoiskDb.searchQueryDao.delete(searchQuery.toSearchQueryEntity())
        kinopoiskDb.movieDao.deleteAllByQuery(searchQuery.text)
    }

    suspend fun deleteAllSearchQueries() {
        kinopoiskDb.searchQueryDao.deleteAll()
        kinopoiskDb.movieDao.deleteAllByQueries()
    }
}