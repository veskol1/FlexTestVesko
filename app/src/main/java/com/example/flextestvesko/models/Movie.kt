package com.example.flextestvesko.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "favorite_movies_table")
data class Movie(
    @PrimaryKey
    val id: Int,

    @SerializedName("original_title")
    val title: String,

    @SerializedName("overview")
    val overview: String,

    @SerializedName("poster_path")
    val imgUrl: String,

    @SerializedName("backdrop_path")
    val imgLandUrl: String,

    val isFavorite: Boolean = false
    )
