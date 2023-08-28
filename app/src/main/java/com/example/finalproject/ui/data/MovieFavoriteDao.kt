package com.example.finalproject.ui.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieFavoriteDao {
    @Insert
    suspend fun insertMovieFav(movieFav: MovieFavorite)

    @Query("DELETE FROM movies_fav WHERE movie_id = :movieId AND user_id = :userId")
    suspend fun deleteFavoriteById(movieId: Int, userId: Int)

    @Query("SELECT * FROM movies_fav WHERE user_id = :userId")
    suspend fun getAllByUserId(userId: Int): List<MovieFavorite>?
}