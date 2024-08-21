package com.example.kinopoiskapi.data.local.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.kinopoiskapi.data.local.RemoteKey

@Dao
interface RemoteKeyDao {
    @Upsert
    suspend fun upsert(remoteKey: RemoteKey)

    @Query("SELECT * FROM remotekey WHERE movieName = :query")
    suspend fun getRemoteKeyByQuery(query: String): RemoteKey?

    @Query("DELETE FROM remotekey")
    suspend fun deleteAll()
}