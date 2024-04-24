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

    private val TAG = "MOVIE_API"

    suspend fun getUpcoming(page: Int = 1): ArrayList<Movie> {
        return localCache.getUpcoming(page) ?: try {
            val response = remoteDataSource.getUpcoming()
            if (response.isSuccessful) {
                response.body()?.let{ response ->
                    localCache.insertUpcoming(page, response.results)
                    response.results
                } ?: arrayListOf()
            } else {
                Log.e(TAG, response.message())
                arrayListOf()
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "Unknown error")
            arrayListOf()
        }
    }

}