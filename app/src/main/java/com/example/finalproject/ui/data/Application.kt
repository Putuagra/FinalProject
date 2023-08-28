package com.example.finalproject.ui.data

import android.app.Application
import androidx.room.Room

class MyApplication : Application() {
    val database by lazy {
        Room.databaseBuilder(this, MovieDatabase::class.java, "AppDatabase")
            .build()
    }
}