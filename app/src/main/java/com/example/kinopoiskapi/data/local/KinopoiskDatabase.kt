package com.example.kinopoiskapi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kinopoiskapi.data.local.daos.GenreDao
import com.example.kinopoiskapi.data.local.daos.MovieDao
import com.example.kinopoiskapi.data.local.daos.RemoteKeyDao
import com.example.kinopoiskapi.data.local.daos.SearchQueryDao

@Database(
    entities = [MovieEntity::class, RemoteKey::class, SearchQueryEntity::class, GenreEntity::class],
    version = 8
)
abstract class KinopoiskDatabase: RoomDatabase() {

    abstract val movieDao: MovieDao
    abstract val remoteKeyDao: RemoteKeyDao
    abstract val searchQueryDao: SearchQueryDao
    abstract val genreDao: GenreDao

}