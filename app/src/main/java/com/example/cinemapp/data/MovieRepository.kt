package com.example.cinemapp.data

import android.util.Log
import retrofit2.Response

class MovieRepository(
    private val localCache: MovieLocalCache,
    private val remoteDataSource: MovieRemoteDataSource
) {

    private fun <T> getBodyFromResponse(response: Response<T>): T? {
        return try {
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e(TAG, response.message())
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "Unknown error")
            null
        }
    }

    suspend fun getCredits(movieId: Int): MovieCreditsDTO? {
        return getBodyFromResponse(remoteDataSource.getMovieCredits(movieId = movieId))
    }

    suspend fun getMovieDetails(movieId: Int): MovieDetailsDTO? {
        return getBodyFromResponse(remoteDataSource.getMovieDetails(movieId = movieId))
    }

    suspend fun getUpcoming(page: Int = 1): List<MovieDTO>? {
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

    suspend fun getImages(movieId: Int): List<ImageDTO>? {
        return try {
            val response = remoteDataSource.getMovieImages(movieId = movieId)
            if (response.isSuccessful) {
                response.body()?.backdrops
            } else {
                Log.e(TAG, response.message())
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "Unknown error")
            null
        }
    }

    suspend fun getVideos(movieId: Int): List<VideoDTO>? {
        return try {
            val response = remoteDataSource.getMovieVideos(movieId = movieId)
            if (response.isSuccessful) {
                response.body()?.results
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