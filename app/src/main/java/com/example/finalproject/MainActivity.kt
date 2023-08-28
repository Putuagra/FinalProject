@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.finalproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.finalproject.ui.data.MyApplication
import com.example.finalproject.ui.navigation.NavGraph

class MainActivity : ComponentActivity() {
    private lateinit var _navController: NavHostController
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val myApp = application as MyApplication
        val appDatabase = myApp.database
        val userDao = appDatabase.userDao()
        setContent {
            _navController = rememberNavController()
            NavGraph(navController = _navController, userDao)
        }
    }
}
