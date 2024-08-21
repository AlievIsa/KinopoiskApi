package com.example.kinopoiskapi.data.remote

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.kinopoiskapi.data.local.KinopoiskDatabase
import com.example.kinopoiskapi.data.local.MovieEntity
import com.example.kinopoiskapi.data.local.RemoteKey
import com.example.kinopoiskapi.data.mappers.toMovieEntity
import com.example.kinopoiskapi.domain.NetworkError

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val query: String,
    private val type: Int,
    private val year: String?,
    private val rate: IntRange,
    private val genre: String?,
    private val kinopoiskDb: KinopoiskDatabase,
    private val kinopoiskApi: ApiService
): RemoteMediator<Int, MovieEntity>() {

    private val movieDao = kinopoiskDb.movieDao
    private val remoteKeyDao = kinopoiskDb.remoteKeyDao

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {

            val loadKey = when(loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    val remoteKey = kinopoiskDb.withTransaction {
                        remoteKeyDao.getRemoteKeyByQuery(query)
                    }
                    if (remoteKey == null) {
                        1
                    } else {
                        if (remoteKey.nextKey == null) {
                            return MediatorResult.Success(
                                endOfPaginationReached = true
                            )
                        }
                        remoteKey.nextKey
                    }
                }
            }

            val response = kinopoiskApi.getMoviesResponse(
                query = query,
                page = loadKey,
                limit = state.config.pageSize,
                type = type,
                genre = genre,
                year = year,
                rateRange = rate
            )

//            Log.d("Check", "$response")

            if (response.docs.isEmpty() && loadKey == 1) {
                return MediatorResult.Success(
                    endOfPaginationReached = true
                )
            }

            kinopoiskDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.deleteAll()
                    movieDao.deleteAllByQuery(query)
                    movieDao.deleteUnnecessary()
                }

                remoteKeyDao.upsert(RemoteKey(query, response.page + 1))
                val movieEntities = response.docs.map {
                    Log.d("Check", "$it")
                    it.toMovieEntity(query)
                }
                kinopoiskDb.movieDao.upsertAll(movieEntities)

//                movieEntities.forEach {
//                    Log.d("upsert", "id = ${it.id}\n" +
//                            "dtoId = ${it.dtoId}\n" +
//                            "name = ${it.name}\n")
//                }
            }

            MediatorResult.Success(
                endOfPaginationReached = response.page == response.pages
            )
            } catch (e: NetworkError.NoInternetConnection) {
            MediatorResult.Error(e)
        } catch (e: NetworkError.HttpError) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(NetworkError.UnknownError(e.message ?: "Unknown error"))
        }
    }
}