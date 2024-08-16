package com.example.kinopoiskapi.data.remote

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
    private val jsonAdapter: JsonAdapter<MoviesResponse> = Moshi.Builder().build()
        .adapter(Types.newParameterizedType(MoviesResponse::class.java))

    override suspend fun getMoviesResponse(query: String, page: Int, limit: Int): MoviesResponse {
        val url =
            if (query == "")
                "https://api.kinopoisk.dev/v1.4/movie?page=$page&limit=$limit"
            else
                "https://api.kinopoisk.dev/v1.4/movie/search?page=$page&limit=$limit&query=${
                    withContext(Dispatchers.IO) {
                        URLEncoder.encode(query, "UTF-8")
                    }
                }"

        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("accept", "application/json")
            .addHeader("X-API-KEY", apiKey)
            .build()

        return suspendCoroutine { continuation ->
            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        continuation.resumeWithException(NetworkError.HttpError(response.code, response.message))
                        return
                    }

                    val body = response.body
                    if (body != null) {
                        val moviesResponse = jsonAdapter.fromJson(body.source())
                        if (moviesResponse != null) {
                            continuation.resume(moviesResponse)
                        } else {
                            continuation.resumeWithException(NetworkError.UnknownError("Failed to parse movies"))
                        }
                    } else {
                        continuation.resumeWithException(NetworkError.UnknownError("Empty response body"))
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(NetworkError.NoInternetConnection())
                }
            })
        }
    }
}