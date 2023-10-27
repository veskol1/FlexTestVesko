package com.example.flextestvesko.di

import android.content.Context
import androidx.room.Room
import com.example.flextestvesko.api.MovieApi
import com.example.flextestvesko.connectivity.NetworkConnectivityManager
import com.example.flextestvesko.repositories.MovieRepository
import com.example.flextestvesko.room.MovieDao
import com.example.flextestvesko.room.MovieDatabase
import com.example.flextestvesko.utils.Constants.BASE_URL

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModules {

    @Provides
    @Singleton
    fun moviesServiceProvide(): MovieApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun moviesRepositoryProvide(newApi: MovieApi, movieDao: MovieDao): MovieRepository {
        return MovieRepository(newApi, movieDao)
    }

    @Provides
    fun provideDao(movieDatabase: MovieDatabase) : MovieDao = movieDatabase.movieDao()

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext appContext: Context): MovieDatabase {
        return Room.databaseBuilder(appContext,
            MovieDatabase::class.java, "database name"
        ).build()
    }

    @Provides
    @Singleton
    fun provideConnectivity(@ApplicationContext appContext: Context): NetworkConnectivityManager {
        return NetworkConnectivityManager(appContext)
    }
}