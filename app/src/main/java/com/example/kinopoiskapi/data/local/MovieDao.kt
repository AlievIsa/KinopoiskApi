package com.example.kinopoiskapi.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MovieDao {

    @Upsert
    suspend fun upsertAll(movies: List<MovieEntity>)

    @Query("SELECT * FROM movieentity")
    fun pagingSource(): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM movieentity WHERE id = :id")
    suspend fun getMovieById(id: Int): MovieEntity

    @Query("DELETE FROM movieentity")
    suspend fun deleteAll()

}