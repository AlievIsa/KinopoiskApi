package com.example.kinopoiskapi.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MovieDao {

    @Upsert
    suspend fun upsertAll(movies: List<MovieEntity>)

    @Query("SELECT * FROM movieentity WHERE `query` LIKE :query")
    fun pagingSource(query: String): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM movieentity WHERE id = :id")
    suspend fun getMovieById(id: Int): MovieEntity

    @Query("DELETE FROM movieentity WHERE `query` LIKE :query")
    suspend fun deleteAllByQuery(query: String)

    @Query("DELETE FROM movieentity WHERE `query` !=''")
    suspend fun deleteAllByQueries()

    @Query("DELETE FROM movieentity WHERE `query` NOT IN (SELECT DISTINCT text FROM searchqueryentity) AND `query` != ''")
    suspend fun deleteUnnecessary()

    @Query("DELETE FROM movieentity")
    suspend fun deleteAll()

}