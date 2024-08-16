package com.example.kinopoiskapi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MovieEntity::class, RemoteKey::class, SearchQueryEntity::class],
    version = 6
)
abstract class KinopoiskDatabase: RoomDatabase() {

    abstract val movieDao: MovieDao
    abstract val remoteKeyDao: RemoteKeyDao
    abstract val searchQueryDao: SearchQueryDao

}