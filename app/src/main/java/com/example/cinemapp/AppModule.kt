package com.example.cinemapp

import com.example.cinemapp.data.MovieLocalCache
import com.example.cinemapp.data.IMovieRemoteDataSource
import com.example.cinemapp.data.MovieRepository
import com.example.cinemapp.data.UserPreferences
import com.example.cinemapp.ui.main.MainViewModel
import com.example.cinemapp.ui.authentication.AuthenticationViewModel
import com.example.cinemapp.ui.main.actor_details.ActorDetailsViewModel
import com.example.cinemapp.ui.main.actor_details.CreditsAdapter
import com.example.cinemapp.ui.main.movie_details.MovieDetailsViewModel
import com.example.cinemapp.ui.main.home.HomeViewModel
import com.example.cinemapp.ui.main.movie_details.CastAdapter
import com.example.cinemapp.ui.main.collections.CollectionsViewModel
import com.example.cinemapp.ui.main.search.SearchViewModel
import com.example.cinemapp.ui.main.settings.appearance.SettingsAppearanceViewModel
import com.example.cinemapp.ui.main.settings.language.SettingsLanguageViewModel
import com.example.cinemapp.ui.splash.SplashViewModel
import com.example.cinemapp.util.UserDataUtil
import com.example.cinemapp.util.mappers.ActorDetailsMapper
import com.example.cinemapp.util.mappers.AuthenticationMapper
import com.example.cinemapp.util.mappers.MovieDetailsMapper
import com.example.cinemapp.util.mappers.MovieListMapper
import com.example.cinemapp.util.mappers.SearchMapper
import com.example.cinemapp.util.mappers.MediaUrlMapper
import com.example.cinemapp.util.mappers.ProfileMapper
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
    single { UserPreferences(androidContext(), get()) }
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
            .create(IMovieRemoteDataSource::class.java)
    }
    single {
        MovieRepository(get(), get(), get())
    }
    single { MediaUrlMapper() }
    single { SearchMapper(get()) }
    single { MovieListMapper(get()) }
    single { MovieDetailsMapper(get()) }
    single { ActorDetailsMapper(get()) }
    single { AuthenticationMapper() }
    single { ProfileMapper(get()) }
    single { UserDataUtil() }
    single { CastAdapter(get()) }
    single { CreditsAdapter() }
    viewModel {
        MainViewModel(get(), get(), get())
    }
    viewModel {
        SplashViewModel(get(), get())
    }
    viewModel {
        HomeViewModel(get(), get())
    }
    viewModel {
        MovieDetailsViewModel(get(), get(), get(), get())
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
    viewModel {
        CollectionsViewModel(get(), get(), get())
    }
    viewModel {
        SettingsAppearanceViewModel(get())
    }
    viewModel {
        SettingsLanguageViewModel(get())
    }
}