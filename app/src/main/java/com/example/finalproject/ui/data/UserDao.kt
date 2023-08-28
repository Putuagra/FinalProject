package com.example.finalproject.ui.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun register(user: User)

    @Query("SELECT * FROM user WHERE email = :email")
    suspend fun getAccountByEmail(email: String): User?

    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    suspend fun getAccountByEmailAndPassword(email: String, password: String): User?

}