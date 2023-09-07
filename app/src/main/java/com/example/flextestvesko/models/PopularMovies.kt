package com.example.flextestvesko.models

import com.google.gson.annotations.SerializedName

data class PopularMovies(
    val page: Int?,
    @SerializedName("results")
    val popularMovies: List<Movie>,
    val total_pages: Int?,
    val total_results: Int?,
)


