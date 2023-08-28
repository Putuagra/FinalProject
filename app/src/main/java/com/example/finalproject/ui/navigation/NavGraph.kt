package com.example.finalproject.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.example.finalproject.ui.viewModel.AuthViewModel

@ExperimentalMaterial3Api
@Composable
fun NavGraph(
    navController: NavHostController,
    userDao: UserDao
) {
    val authViewModel = viewModel<AuthViewModel>()
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(
            route = Screen.Home.route
        ) {
            MovieScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(
            route = Screen.Profile.route
        ) {
            ProfileScreen(navController = navController, authViewModel = authViewModel)
        }
        composable(
            route = Screen.Login.route
        ) {
            Login(navController = navController, userDao = userDao, authViewModel = authViewModel)
        }
        composable(
            route = Screen.Register.route
        ) {
            RegisterScreen(navController = navController, userDao = userDao)
        }
        composable(route = Screen.Detail.route) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")
            itemId?.let { itemId ->
                DetailScreen(
                    navController = navController, itemId = itemId, authViewModel = authViewModel
                )
            }
        }
        composable(
            route = Screen.Favorite.route
        ) {
            FavoriteScreen(navController = navController, authViewModel = authViewModel)
        }
    }
}
