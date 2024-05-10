package com.example.cinemapp.data

import android.util.Log
import com.example.cinemapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieRepository(
    private val localCache: MovieLocalCache,
    private val remoteDataSource: MovieRemoteDataSource
) {

    suspend fun getUpcoming(page: Int = 1): List<Movie>? {
        return localCache.getUpcoming(page) ?: try {
            val response = remoteDataSource.getUpcoming(page = page)
            if (response.isSuccessful) {
                response.body()?.let { movieResponse ->
                    localCache.insertUpcoming(page, movieResponse.results)
                    movieResponse.results
                }
            } else {
                Log.e(TAG, response.message())
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "Unknown error")
            null
        }
    }

    companion object {
        private const val TAG = "MOVIE_API"
    }
}