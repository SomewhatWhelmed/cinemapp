package com.example.cinemapp.data

import android.util.Log
import com.example.cinemapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MovieRepository {

    private val BASE_URL = "https://api.themoviedb.org/"
    private val TAG = "MOVIE_API"

    private val localCache: MovieLocalCache = MovieLocalCache()

    private val httpClient = OkHttpClient.Builder().apply {
        addInterceptor(
            Interceptor { chain ->
                val builder = chain.request().newBuilder()
                builder.header("accept", "application/json")
                builder.header("Authorization", "Bearer ${BuildConfig.TMDB_API_KEY}")
                return@Interceptor chain.proceed(builder.build())
            }
        )
    }

    private val remoteDataSource = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient.build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MovieRemoteDataSource::class.java)


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