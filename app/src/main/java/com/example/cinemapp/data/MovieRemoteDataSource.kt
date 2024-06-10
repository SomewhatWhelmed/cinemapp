package com.example.cinemapp.data

import com.example.cinemapp.data.model.AccountDetailsDTO
import com.example.cinemapp.data.model.ImagesResponseDTO
import com.example.cinemapp.data.model.MovieCreditsDTO
import com.example.cinemapp.data.model.MovieDetailsDTO
import com.example.cinemapp.data.model.MovieResponseDTO
import com.example.cinemapp.data.model.PersonDetailsDTO
import com.example.cinemapp.data.model.PersonMovieCreditsResponseDTO
import com.example.cinemapp.data.model.RequestTokenResponseDTO
import com.example.cinemapp.data.model.SearchPersonResponseDTO
import com.example.cinemapp.data.model.SessionDeleteBodyDTO
import com.example.cinemapp.data.model.SessionRequestDTO
import com.example.cinemapp.data.model.SessionResponseDTO
import com.example.cinemapp.data.model.ValidateWithLoginRequestDTO
import com.example.cinemapp.data.model.VideoResponseDTO
import com.example.cinemapp.ui.main.model.SessionDeleteResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
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

    @GET("$API_VERSION/search/person")
    suspend fun getSearchPersonResults(
        @Query("query") query: String,
        @Query("page") page:Int = 1
    ): Response<SearchPersonResponseDTO>

    @GET("$API_VERSION/search/movie")
    suspend fun getSearchMovieResults(
        @Query("query") query: String,
        @Query("page") page:Int = 1
    ): Response<MovieResponseDTO>

    @GET("$API_VERSION/authentication/token/new")
    suspend fun getRequestToken(): Response<RequestTokenResponseDTO>


    @POST("$API_VERSION/authentication/token/validate_with_login")
    suspend fun validateRequestTokenLogin(
        @Body body: ValidateWithLoginRequestDTO
    ): Response<RequestTokenResponseDTO>

    @POST("$API_VERSION/authentication/session/new")
    suspend fun createSession(
        @Body body: SessionRequestDTO
    ): Response<SessionResponseDTO>

//    @DELETE("$API_VERSION/authentication/session")
    @HTTP(method = "DELETE", path = "$API_VERSION/authentication/session", hasBody = true)
    suspend fun deleteSession(
        @Body body: SessionDeleteBodyDTO
    ): Response<SessionDeleteResponseDTO>

    @GET("$API_VERSION/account")
    suspend fun getAccountDetails(
        @Query("session_id") sessionId: String
    ): Response<AccountDetailsDTO>

    companion object {
        const val API_VERSION = 3
    }
}