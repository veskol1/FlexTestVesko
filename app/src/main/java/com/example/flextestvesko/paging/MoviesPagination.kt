package com.example.flextestvesko.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.flextestvesko.api.MovieApi
import com.example.flextestvesko.models.Movie
import com.example.flextestvesko.viewmodels.ListType


class MoviesPagination(
    private val api: MovieApi,
    private val listType: ListType,
) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {

        return try {
            val currentPage = params.key ?: 1
            val responseData = mutableListOf<Movie>()

            val response = when (listType) {
                ListType.POPULAR -> api.getPopularMovies(currentPage)
                else -> api.getNewMovies(currentPage)
            }

            val data = response.body()?.popularMovies ?: emptyList()
            responseData.addAll(data)

            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}