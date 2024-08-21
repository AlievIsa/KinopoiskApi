package com.example.kinopoiskapi.data.local.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.kinopoiskapi.data.local.GenreEntity

@Dao
interface GenreDao {

    @Upsert
    suspend fun upsertGenres(genres: List<GenreEntity>)

    @Query("SELECT * FROM GenreEntity")
    suspend fun getGenres(): List<GenreEntity>

}