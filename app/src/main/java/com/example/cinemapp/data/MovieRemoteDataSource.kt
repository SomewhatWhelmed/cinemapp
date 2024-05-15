package com.example.cinemapp.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieRemoteDataSource {

    @GET("3/movie/upcoming")
    suspend fun getUpcoming(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<MovieResponse>

    @GET("3/movie/{movieId}")
    suspend fun getMovieDetails(@Path("movieId") movieId: Int): Response<MovieDetailsDTO>
}