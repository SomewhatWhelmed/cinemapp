package com.example.cinemapp.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieRemoteDataSource {

    @GET("$API_VERSION/movie/upcoming")
    suspend fun getUpcoming(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<MovieResponseDTO>

    @GET("$API_VERSION/movie/popular")
    suspend fun getPopular(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<MovieResponseDTO>

    @GET("$API_VERSION/movie/top_rated")
    suspend fun getTopRated(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<MovieResponseDTO>

    @GET("$API_VERSION/movie/{movieId}")
    suspend fun getMovieDetails(@Path("movieId") movieId: Int): Response<MovieDetailsDTO>

    @GET("$API_VERSION/movie/{movieId}/credits")
    suspend fun getMovieCredits(@Path("movieId") movieId: Int): Response<MovieCreditsDTO>

    @GET("$API_VERSION/person/{person_id}")
    suspend fun getPersonDetails(@Path("person_id") personId: Int): Response<PersonDetailsDTO>

    @GET("$API_VERSION/movie/{movieId}/images")
    suspend fun getMovieImages(
        @Path("movieId") movieId: Int,
        @Query("language") language: String = "en"
    ): Response<ImagesResponseDTO>

    @GET("$API_VERSION/movie/{movieId}/videos")
    suspend fun getMovieVideos(
        @Path("movieId") movieId: Int,
        @Query("language") language: String = "en"
    ): Response<VideoResponseDTO>

    @GET("$API_VERSION/person/{personId}/movie_credits")
    suspend fun getPersonMovieCredits(
        @Path("personId") personId: Int
    ): Response<PersonMovieCreditsResponseDTO>

    companion object {
        const val API_VERSION = 3
    }
}