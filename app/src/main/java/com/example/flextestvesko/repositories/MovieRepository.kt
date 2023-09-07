package com.example.flextestvesko.repositories


import androidx.paging.PagingSource
import com.example.flextestvesko.api.MovieApi
import com.example.flextestvesko.models.Movie
import com.example.flextestvesko.paging.MoviesPagination
import com.example.flextestvesko.room.MovieDao
import com.example.flextestvesko.viewmodels.ListType

import javax.inject.Inject

class MovieRepository @Inject constructor(private val movieApi: MovieApi, private val movieDao: MovieDao) {

    fun getRemoteMovieList(listType: ListType) =  MoviesPagination(api = movieApi, listType = listType)

    fun getFavoriteMovieList(): PagingSource<Int, Movie> {
        return movieDao.pagingSource()
    }

    fun insertMovieToDb(movie: Movie) {
        movieDao.insert(movie = movie)
    }

    fun deleteMovieToDb(movie: Movie) {
        movieDao.delete(movie = movie)
    }

    fun checkIfMovieIsFavorite(movie: Movie): Boolean {
        return (movieDao.getAll().find { it.id == movie.id } != null)
    }

}