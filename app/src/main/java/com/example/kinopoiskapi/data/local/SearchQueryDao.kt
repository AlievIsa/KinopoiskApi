package com.example.kinopoiskapi.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchQueryDao {

    @Upsert
    suspend fun upsert(searchQueryEntity: SearchQueryEntity)

    @Query("SELECT * FROM searchqueryentity WHERE text LIKE '%' || :query || '%' OR :query = ''")
    fun getSearchQueries(query: String): Flow<List<SearchQueryEntity>>

    @Delete
    suspend fun delete(searchQueryEntity: SearchQueryEntity)

    @Query("DELETE FROM searchqueryentity")
    suspend fun deleteAll()
}