package com.example.finalproject

sealed class Screen(val route: String){
    object Home: Screen(route = "movie_screen")
    object Login: Screen(route = "login_screen")
    object Register: Screen(route = "register_screen")
    object Detail: Screen(route = "detail_screen/{itemId}")
    object Profile: Screen(route = "profile_screen")
    object Favorite: Screen(route = "favorite_screen")
}