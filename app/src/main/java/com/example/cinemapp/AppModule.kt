package com.example.cinemapp

import com.example.cinemapp.data.MovieLocalCache
import com.example.cinemapp.data.MovieRemoteDataSource
import com.example.cinemapp.data.MovieRepository
import com.example.cinemapp.ui.splash.SplashViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.core.qualifier.qualifier
import org.koin.core.scope.get
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single<String> (named("BASE_URL")) {
        "https://api.themoviedb.org/"
    }
    single {
        MovieLocalCache()
    }
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
    viewModel {
        SplashViewModel(get())
    }
}