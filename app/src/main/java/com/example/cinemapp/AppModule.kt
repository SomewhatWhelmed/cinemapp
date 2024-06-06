package com.example.cinemapp

import com.example.cinemapp.data.MovieLocalCache
import com.example.cinemapp.data.MovieRemoteDataSource
import com.example.cinemapp.data.MovieRepository
import com.example.cinemapp.data.UserPreferences
import com.example.cinemapp.ui.authentication.AuthenticationViewModel
import com.example.cinemapp.ui.main.ActorDetailsViewModel
import com.example.cinemapp.ui.main.MovieDetailsViewModel
import com.example.cinemapp.ui.main.HomeViewModel
import com.example.cinemapp.ui.main.SearchViewModel
import com.example.cinemapp.ui.splash.SplashViewModel
import com.example.cinemapp.util.mappers.ActorDetailsMapper
import com.example.cinemapp.util.mappers.AuthenticationMapper
import com.example.cinemapp.util.mappers.MovieDetailsMapper
import com.example.cinemapp.util.mappers.HomeMapper
import com.example.cinemapp.util.mappers.SearchMapper
import com.example.cinemapp.util.mappers.MediaUrlMapper
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single<String>(named("BASE_URL")) {
        BuildConfig.URL_BASE
    }
    single { MovieLocalCache() }
    single { UserPreferences(androidContext()) }
    single {
        OkHttpClient.Builder().apply {
            addInterceptor(
                Interceptor { chain ->
                    val builder = chain.request().newBuilder()
                    builder.header("accept", "application/json")
                    builder.header("Authorization", "Bearer ${BuildConfig.TMDB_API_KEY}")
                    return@Interceptor chain.proceed(builder.build())
                }
            )
        }
    }
    single {
        Retrofit.Builder()
            .baseUrl(get(qualifier = named("BASE_URL")) as String)
            .client(get<OkHttpClient.Builder>().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieRemoteDataSource::class.java)
    }
    single {
        MovieRepository(get(), get())
    }
    single { MediaUrlMapper() }
    single { SearchMapper(get()) }
    single { HomeMapper(get()) }
    single { MovieDetailsMapper(get()) }
    single { ActorDetailsMapper(get()) }
    single { AuthenticationMapper() }
    viewModel {
        SplashViewModel(get())
    }
    viewModel {
        HomeViewModel(get(), get())
    }
    viewModel {
        MovieDetailsViewModel(get(), get())
    }
    viewModel {
        ActorDetailsViewModel(get(), get())
    }
    viewModel {
        SearchViewModel(get(), get())
    }
    viewModel {
        AuthenticationViewModel(get(), get(), get())
    }
}