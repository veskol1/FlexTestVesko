package com.example.flextestvesko.utils

object Constants {
    const val BASE_URL = "https://api.themoviedb.org/"
    private const val API_KEY = "d2bc56bb74d10fcca04542127ebda98c"

    const val POPULAR_MOVIES_END_POINT = "3/movie/popular?api_key=$API_KEY"
    const val NEW_MOVIES_END_POINT = "3/movie/now_playing?api_key=$API_KEY"

    const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342/"
    const val POSTER_LAND_BASE_URL = "https://image.tmdb.org/t/p/w780/"

    const val POPULAR_MOVIE = 0
    const val NEW_MOVIE = 1
    const val FAVORITE_MOVIE = 2


}