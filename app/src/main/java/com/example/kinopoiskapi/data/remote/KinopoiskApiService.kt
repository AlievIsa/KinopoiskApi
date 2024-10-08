package com.example.kinopoiskapi.data.remote

import android.util.Log
import com.example.kinopoiskapi.domain.NetworkError
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.URLEncoder
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class KinopoiskApiService(private val okHttpClient: OkHttpClient, private val apiKey: String): ApiService {

    private suspend inline fun <reified T> performRequest(
        request: Request,
        jsonAdapter: JsonAdapter<T>
    ): T {
        return suspendCoroutine { continuation ->
            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        continuation.resumeWithException(
                            NetworkError.HttpError(response.code, response.message)
                        )
                        return
                    }

                    val body = response.body
                    if (body != null) {
                        val result = jsonAdapter.fromJson(body.source())
                        if (result != null) {
                            continuation.resume(result)
                        } else {
                            continuation.resumeWithException(
                                NetworkError.UnknownError("Failed to parse response")
                            )
                        }
                    } else {
                        continuation.resumeWithException(
                            NetworkError.UnknownError("Empty response body")
                        )
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(NetworkError.NoInternetConnection())
                }
            })
        }
    }

    private fun createRequest(url: String): Request {
        return Request.Builder()
            .url(url)
            .get()
            .addHeader("accept", "application/json")
            .addHeader("X-API-KEY", apiKey)
            .build()
    }

    override suspend fun getMoviesResponse(
        query: String,
        page: Int,
        limit: Int,
        type: Int,
        year: String?,
        rateRange: IntRange,
        genre: String?
    ): MoviesResponse {
        val url = if (query.isNotBlank() && query.isNotEmpty()) {
            "https://api.kinopoisk.dev/v1.4/movie/search?page=$page&limit=$limit&query=${
                withContext(Dispatchers.IO) {
                    URLEncoder.encode(query, "UTF-8")
                }
            }"
        } else {
            val baseUrl = "https://api.kinopoisk.dev/v1.4/movie?page=$page&limit=$limit&sortField=votes.kp&sortField=rating.kp&sortType=-1&sortType=-1"

            val queryParams = mutableListOf<String>()
            val typeStr = when(type) {
                1 -> "movie"
                2 -> "tv-series"
                else -> null
            }
            typeStr?.let {
                queryParams.add("type=$it") }
            year?.let { queryParams.add("year=$it") }
            val rate = if (rateRange == 0..10) null else rateRange
            rate?.let {
                val rateString = if (it.first != it.last) "${it.first}-${it.last}" else it.first.toString()
                queryParams.add("rating.kp=$rateString")
            }
            genre?.let {
                queryParams.add("genres.name=${URLEncoder.encode(it, "UTF-8")}")
            }

            if (queryParams.isNotEmpty()) {
                "$baseUrl&${queryParams.joinToString("&")}"
            } else {
                baseUrl
            }
        }

        Log.d("URL", url)

        val request = createRequest(url)
        val jsonAdapter: JsonAdapter<MoviesResponse> = Moshi.Builder().build()
            .adapter(MoviesResponse::class.java)

        return performRequest(request, jsonAdapter)
    }

    override suspend fun getGenres(): List<GenreDto> {
        val url = "https://api.kinopoisk.dev/v1/movie/possible-values-by-field?field=genres.name"
        val request = createRequest(url)
        val jsonAdapter: JsonAdapter<List<GenreDto>> = Moshi.Builder().build()
            .adapter(Types.newParameterizedType(List::class.java, GenreDto::class.java))

        return performRequest(request, jsonAdapter)
    }

    override suspend fun getSeasonsNum(serialId: Int): SeasonResponse {
        val url = "https://api.kinopoisk.dev/v1.4/season?page=1&limit=1&movieId=$serialId"
        val request = createRequest(url)
        val jsonAdapter: JsonAdapter<SeasonResponse> = Moshi.Builder().build()
            .adapter(Types.newParameterizedType(SeasonResponse::class.java))

        return performRequest(request, jsonAdapter)
    }
}