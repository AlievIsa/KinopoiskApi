package com.example.kinopoiskapi.di

import android.content.Context
import androidx.room.Room
import com.example.kinopoiskapi.BuildConfig
import com.example.kinopoiskapi.data.local.KinopoiskDatabase
import com.example.kinopoiskapi.data.remote.ApiService
import com.example.kinopoiskapi.data.remote.KinopoiskApiService
import com.example.kinopoiskapi.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): KinopoiskDatabase {
        return Room.databaseBuilder(
            context,
            KinopoiskDatabase::class.java,
            "kinopoisk.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient()
    }

    @Provides
    @Singleton
    fun provideMovieApi(
        okHttpClient: OkHttpClient,
    ): ApiService {
        val apiKey = BuildConfig.API_KEY
        return KinopoiskApiService(okHttpClient, apiKey)
    }

    @Provides
    @Singleton
    fun provideRepository(kinopoiskDb: KinopoiskDatabase, kinopoiskApi: ApiService): Repository {
        return Repository(kinopoiskDb, kinopoiskApi)
    }
}