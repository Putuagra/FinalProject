package com.example.finalproject.ui.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies_fav")
data class MovieFavorite(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val movie_id: Int,
    val title: String,
    val poster_path: String,
    val overview: String,
    val release_date: String,
    val user_id: Int
)