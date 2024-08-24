package com.example.cinemapp.data

import android.util.Log
import com.example.cinemapp.data.model.AccountDetailsDTO
import com.example.cinemapp.data.model.CastMovieCreditDTO
import com.example.cinemapp.data.model.ImageDTO
import com.example.cinemapp.data.model.MovieCreditsDTO
import com.example.cinemapp.data.model.MovieDTO
import com.example.cinemapp.data.model.MovieDetailsDTO
import com.example.cinemapp.data.model.MovieResponseDTO
import com.example.cinemapp.data.model.PersonDTO
import com.example.cinemapp.data.model.PersonDetailsDTO
import com.example.cinemapp.data.model.RatingRequestBodyDTO
import com.example.cinemapp.data.model.RequestTokenResponseDTO
import com.example.cinemapp.data.model.SessionDeleteBodyDTO
import com.example.cinemapp.data.model.SessionRequestDTO
import com.example.cinemapp.data.model.SessionResponseDTO
import com.example.cinemapp.data.model.SetFavoriteBodyDTO
import com.example.cinemapp.data.model.SetWatchlistBodyDTO
import com.example.cinemapp.data.model.StatusResponseDTO
import com.example.cinemapp.data.model.ValidateWithLoginRequestDTO
import com.example.cinemapp.data.model.VideoDTO
import com.example.cinemapp.ui.main.model.SessionDeleteResponseDTO
import retrofit2.Response

class MovieRepository(
    private val localCache: MovieLocalCache,
    private val remoteDataSource: IMovieRemoteDataSource,
    private val userPreferences: UserPreferences
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

    suspend fun getMovieCredits(movieId: Int): MovieCreditsDTO? {
        return getBodyFromResponse(
            remoteDataSource.getMovieCredits(
                movieId = movieId
            )
        )
    }

    suspend fun getMovieDetails(movieId: Int): MovieDetailsDTO? {
        return getBodyFromResponse(
            remoteDataSource.getMovieDetails(
                movieId = movieId
            )
        )
    }

    suspend fun getMovieList(
        movieListType: MovieListType,
        page: Int = 1,
        sessionId: String? = null
    ): MovieResponseDTO? {
        return localCache.getMovieList(movieListType, page) ?: try {
            val response = when (movieListType) {
                MovieListType.UPCOMING -> remoteDataSource.getUpcoming(
                    page = page,
                    language = userPreferences.getLanguageNotNull()
                )

                MovieListType.POPULAR -> remoteDataSource.getPopular(
                    page = page,
                    language = userPreferences.getLanguageNotNull()
                )

                MovieListType.TOP_RATED -> remoteDataSource.getTopRated(
                    page = page,
                    language = userPreferences.getLanguageNotNull()
                )

                MovieListType.FAVORITE -> sessionId?.let {
                    remoteDataSource.getFavorite(
                        page = page,
                        sessionId = it,
                        language = userPreferences.getLanguageNotNull()
                    )
                }

                MovieListType.WATCHLIST -> sessionId?.let {
                    remoteDataSource.getWatchlist(
                        language = userPreferences.getLanguageNotNull(),
                        page = page,
                        sessionId = it
                    )
                }

                MovieListType.RATED -> sessionId?.let {
                    remoteDataSource.getRated(
                        language = userPreferences.getLanguageNotNull(),
                        page = page,
                        sessionId = it
                    )
                }
            }
            response?.let {
                if (response.isSuccessful) {
                    response.body()?.let { movieResponse ->
                        movieResponse.results?.let { results ->
                            localCache.insert(
                                movieListType,
                                page,
                                results,
                                movieResponse.totalPages
                            )
                        }
                        movieResponse
                    }
                } else {
                    Log.e(TAG, response.message())
                    null
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message ?: "Unknown error")
            null
        }
    }

    suspend fun getMovieListAllPages(
        movieListType: MovieListType,
        sessionId: String? = null
    ): List<MovieDTO> {
        val firstPage = when (movieListType) {
            MovieListType.UPCOMING -> remoteDataSource.getUpcoming()
            MovieListType.POPULAR -> remoteDataSource.getPopular()
            MovieListType.TOP_RATED -> remoteDataSource.getTopRated()
            MovieListType.FAVORITE -> sessionId?.let { remoteDataSource.getFavorite(sessionId = it) }
            MovieListType.WATCHLIST -> sessionId?.let { remoteDataSource.getWatchlist(sessionId = it) }
            MovieListType.RATED -> sessionId?.let { remoteDataSource.getRated(sessionId = it) }
        }?.let {
            getBodyFromResponse(it)
        }
        val fullList = mutableListOf<MovieDTO>()
        for (i in 1..(firstPage?.totalPages ?: 0)) {
            getMovieList(movieListType, i, sessionId)?.let {
                fullList.addAll(
                    it.results ?: emptyList()
                )
            }
        }
        return fullList
    }

    suspend fun getSearchPersonList(query: String, page: Int = 1): List<PersonDTO>? {
        return try {
            val response = remoteDataSource.getSearchPersonResults(
                query = query,
                page = page,
                language = userPreferences.getLanguageNotNull()
            )
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

    suspend fun getSearchMovieList(query: String, page: Int = 1): List<MovieDTO>? {
        return try {
            val response =
                remoteDataSource.getSearchMovieResults(query, page = page, userPreferences.getLanguageNotNull())
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

    suspend fun getPersonDetails(personId: Int): PersonDetailsDTO? {
        return getBodyFromResponse(
            remoteDataSource.getPersonDetails(
                personId = personId
            )
        )
    }

    suspend fun getPersonMovieCreditsYears(personId: Int): List<String?>? {
        return localCache.getPersonMovieCreditsYears(personId)
            ?: try {
                val response = remoteDataSource.getPersonMovieCredits(personId, userPreferences.getLanguageNotNull())
                if (response.isSuccessful) {
                    response.body()?.let { creditsResponse ->
                        localCache.insert(creditsResponse)
                        creditsResponse.cast?.map { credit -> credit.releaseDate } ?: emptyList()
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

    suspend fun getPersonMovieCredits(
        personId: Int,
        year: Int? = null,
        getAll: Boolean = false
    ): List<CastMovieCreditDTO>? {
        return (
            if (getAll) localCache.getPersonMovieCreditsAll(personId)
            else localCache.getPersonMovieCredits(personId, year)
            ) ?: try {
            val response = remoteDataSource.getPersonMovieCredits(personId, userPreferences.getLanguageNotNull())
            if (response.isSuccessful) {
                response.body()?.let { creditsResponse ->
                    localCache.insert(creditsResponse)
                    creditsResponse.cast?.let { cast ->
                        if (getAll) cast
                        else cast.filter { credit ->
                            year?.let { credit.releaseDate?.startsWith(year.toString()) ?: false }
                                ?: credit.releaseDate.isNullOrEmpty()
                        }
                    }
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

    suspend fun getSessionId(username: String, password: String): SessionResponseDTO? {
        getRequestToken()?.let { firstRequestTokenResponse ->
            firstRequestTokenResponse.success?.let { firstSuccess ->
                if (firstSuccess) {
                    firstRequestTokenResponse.requestToken?.let { firstRequestToken ->
                        validateRequestTokenLogin(
                            username,
                            password,
                            firstRequestToken
                        )?.let { secondRequestTokenResponse ->
                            secondRequestTokenResponse.success?.let { secondSuccess ->
                                if (secondSuccess) {
                                    secondRequestTokenResponse.requestToken?.let { secondRequestToken ->
                                        return createSession(secondRequestToken)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return SessionResponseDTO(false, "")
    }

    suspend fun deleteSession(sessionId: String): SessionDeleteResponseDTO? {
        val body = SessionDeleteBodyDTO(sessionId)
        localCache.clearProfileCache()
        return getBodyFromResponse(remoteDataSource.deleteSession(body))
    }

    private suspend fun getRequestToken(): RequestTokenResponseDTO? {
        return getBodyFromResponse(remoteDataSource.getRequestToken())
    }

    private suspend fun validateRequestTokenLogin(
        username: String,
        password: String,
        requestToken: String
    ): RequestTokenResponseDTO? {
        val body = ValidateWithLoginRequestDTO(
            username,
            password,
            requestToken
        )
        return getBodyFromResponse(remoteDataSource.validateRequestTokenLogin(body))
    }

    private suspend fun createSession(requestToken: String): SessionResponseDTO? {
        val body = SessionRequestDTO(requestToken)
        return getBodyFromResponse(remoteDataSource.createSession(body))
    }

    suspend fun getAccountDetails(sessionId: String): AccountDetailsDTO? {
        return getBodyFromResponse(remoteDataSource.getAccountDetails(sessionId))
    }

    private suspend fun setFavorite(
        sessionId: String,
        mediaId: Int,
        setValue: Boolean,
        mediaType: String
    ): StatusResponseDTO? {
        return getBodyFromResponse(
            remoteDataSource.setFavorite(
                sessionId,
                SetFavoriteBodyDTO(mediaType, mediaId, setValue)
            )
        )
    }

    suspend fun setFavoriteMovie(
        sessionId: String,
        movieId: Int,
        setValue: Boolean
    ): StatusResponseDTO? {
        val response = setFavorite(sessionId, movieId, setValue, "movie")
        response?.let {
            localCache.clearCache(MovieListType.FAVORITE)
        }
        return response
    }

    private suspend fun setWatchlist(
        sessionId: String,
        mediaId: Int,
        setValue: Boolean,
        mediaType: String
    ): StatusResponseDTO? {
        return getBodyFromResponse(
            remoteDataSource.setWatchlist(
                sessionId,
                SetWatchlistBodyDTO(mediaType, mediaId, setValue)
            )
        )
    }

    suspend fun setWatchlistMovie(
        sessionId: String,
        movieId: Int,
        setValue: Boolean
    ): StatusResponseDTO? {
        val response = setWatchlist(sessionId, movieId, setValue, "movie")
        response?.let {
            localCache.clearCache(MovieListType.WATCHLIST)
        }
        return response
    }

    suspend fun addMovieRating(sessionId: String, movieId: Int, rating: Int): StatusResponseDTO? {
        val response = getBodyFromResponse(
            remoteDataSource.addRating(
                movieId,
                sessionId,
                RatingRequestBodyDTO(rating.toFloat())
            )
        )
        response?.let {
            localCache.clearCache(MovieListType.RATED)
        }
        return response
    }

    suspend fun deleteRating(sessionId: String, movieId: Int): StatusResponseDTO? {
        val response = getBodyFromResponse(
            remoteDataSource.deleteRating(
                movieId,
                sessionId
            )
        )
        response?.let {
            localCache.clearCache(MovieListType.RATED)
        }
        return response
    }

    companion object {
        private const val TAG = "MOVIE_API"
    }

    enum class MovieListType {
        UPCOMING, POPULAR, TOP_RATED, FAVORITE, WATCHLIST, RATED
    }

}