package com.example.kinopoiskapi.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface RemoteKeyDao {
    @Upsert
    suspend fun upsert(remoteKey: RemoteKey)

    @Query("SELECT * FROM remotekey WHERE movieName = :query")
    suspend fun getRemoteKeyByQuery(query: String): RemoteKey?

    @Query("DELETE FROM remotekey")
    suspend fun deleteAll()
}