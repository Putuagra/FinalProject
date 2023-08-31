package com.example.finalproject.ui.data

import android.app.Application

class Application : Application() {
    val database by lazy {
//        Room.databaseBuilder(this, MovieDatabase::class.java, "AppDatabase")
//            .build()
        MovieDatabase.getDatabase(this)
    }
}