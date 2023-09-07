package com.example.flextestvesko.api


import com.example.flextestvesko.models.PopularMovies
import com.example.flextestvesko.utils.Constants.NEW_MOVIES_END_POINT
import com.example.flextestvesko.utils.Constants.POPULAR_MOVIES_END_POINT
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET(POPULAR_MOVIES_END_POINT)
    suspend fun getPopularMovies(
        @Query ("page") page: Int
    ): Response<PopularMovies>

    @GET(NEW_MOVIES_END_POINT)
    suspend fun getNewMovies(
        @Query ("page") page: Int
    ): Response<PopularMovies>
}