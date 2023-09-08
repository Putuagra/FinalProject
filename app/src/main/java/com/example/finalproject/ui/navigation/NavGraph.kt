package com.example.finalproject.ui.navigation

import android.content.Context
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.finalproject.Screen
import com.example.finalproject.ui.data.UserDao
import com.example.finalproject.ui.screen.DetailScreen
import com.example.finalproject.ui.screen.FavoriteScreen
import com.example.finalproject.ui.screen.Login
import com.example.finalproject.ui.screen.MovieScreen
import com.example.finalproject.ui.screen.ProfileScreen
import com.example.finalproject.ui.screen.RegisterScreen

@ExperimentalMaterial3Api
@Composable
fun NavGraph(
    navController: NavHostController,
    userDao: UserDao
) {
    var loggedIn by remember { mutableStateOf(false) }
    var accountName by remember { mutableStateOf("") }
    var accountEmail by remember { mutableStateOf("") }
    var accountId: Int? = null

    val sharedPreferences =
        LocalContext.current.getSharedPreferences("session", Context.MODE_PRIVATE)
    loggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
    accountId = sharedPreferences.getInt("accountId", -1)
    accountName = sharedPreferences.getString("accountName", "") ?: ""
    accountEmail = sharedPreferences.getString("accountEmail", "") ?: ""

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(
            route = Screen.Home.route
        ) {
            MovieScreen(navController = navController, loggedIn, accountName)
        }
        composable(
            route = Screen.Profile.route
        ) {
            ProfileScreen(navController = navController, loggedIn, accountName, accountEmail){
                loggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
                accountId = sharedPreferences.getInt("accountId", -1)
                accountName = sharedPreferences.getString("accountName", "") ?: ""
                accountEmail = sharedPreferences.getString("accountEmail", "") ?: ""
            }
        }
        composable(
            route = Screen.Login.route
        ) {
            Login(navController = navController, userDao = userDao) {
                loggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
                accountId = sharedPreferences.getInt("accountId", -1)
                accountName = sharedPreferences.getString("accountName", "") ?: ""
                accountEmail = sharedPreferences.getString("accountEmail", "") ?: ""
            }
        }
        composable(
            route = Screen.Register.route
        ) {
            RegisterScreen(navController = navController, userDao = userDao)
        }
        composable(route = Screen.Detail.route) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")
            itemId?.let { itemId ->
                accountId = sharedPreferences.getInt("accountId", -1)
                DetailScreen(
                    navController = navController, itemId = itemId, accountId, loggedIn
                )
            }
        }
        composable(
            route = Screen.Favorite.route
        ) {
            FavoriteScreen(navController = navController, accountId, loggedIn)
        }
    }
}
