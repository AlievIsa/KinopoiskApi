package com.example.kinopoiskapi.domain

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.kinopoiskapi.data.local.KinopoiskDatabase
import com.example.kinopoiskapi.data.local.MovieEntity
import com.example.kinopoiskapi.data.local.SearchQueryEntity
import com.example.kinopoiskapi.data.mappers.toSearchQueryEntity
import com.example.kinopoiskapi.data.remote.ApiService
import com.example.kinopoiskapi.data.remote.MovieRemoteMediator
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalPagingApi::class)
class Repository(
    private val kinopoiskDb: KinopoiskDatabase,
    private val kinopoiskApi: ApiService
) {

    fun getMovieFlowPagingData(query: String): Flow<PagingData<MovieEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = MovieRemoteMediator(
                query = query,
                kinopoiskDb = kinopoiskDb,
                kinopoiskApi = kinopoiskApi
            ),
            pagingSourceFactory = {
                kinopoiskDb.movieDao.pagingSource()
            }
        ).flow
    }

    suspend fun getMovieById(id: Int) = kinopoiskDb.movieDao.getMovieById(id)

    suspend fun upsertSearchQuery(query: String) = kinopoiskDb.searchQueryDao.upsert(SearchQueryEntity(text = query))

    fun getSearchQueries(query: String) = kinopoiskDb.searchQueryDao.getSearchQueries(query)

    suspend fun deleteSearchQuery(searchQuery: SearchQuery) = kinopoiskDb.searchQueryDao.delete(searchQuery.toSearchQueryEntity())

    suspend fun deleteAllSearchQueries() = kinopoiskDb.searchQueryDao.deleteAll()
}