package com.example.finalproject.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.finalproject.Screen
import com.example.finalproject.ui.data.Application
import com.example.finalproject.ui.data.Movie
import com.example.finalproject.ui.viewModel.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(navController: NavController, authViewModel: AuthViewModel) {
    var selectedTab by remember { mutableStateOf(Tab.Favorite) }
    var isHomeNavigationDone by remember { mutableStateOf(false) }
    var isProfileNavigationDone by remember { mutableStateOf(false) }
    val userLoggedIn = authViewModel.userLoggedIn.value
    val userId = authViewModel.loggedInUserId.value
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val database = application.database
    val movieFavDao = database.movieFavoriteDao()

    var movieList by remember { mutableStateOf<List<Movie>?>(null) }

    LaunchedEffect(key1 = true) {
        CoroutineScope(Dispatchers.IO).launch {
            val movieFavList = movieFavDao.getAllByUserId(userId)

            val mappedMovieList = movieFavList?.map { movieFav ->
                Movie(
                    id = movieFav.movie_id,
                    title = movieFav.title,
                    poster_path = movieFav.poster_path,
                    overview = movieFav.overview,
                    vote_average = 0.0,
                    vote_count = 0,
                    genres = null
                )
            }

            withContext(Dispatchers.Main) {
                movieList = mappedMovieList
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    BottomBarItem(
                        icon = Icons.Default.Home,
                        text = "Home",
                        isSelected = selectedTab == Tab.Movies
                    ) {
                        selectedTab = Tab.Movies
                    }
                    BottomBarItem(
                        icon = Icons.Default.Favorite,
                        text = "Favorite",
                        isSelected = selectedTab == Tab.Favorite
                    ) {
                        selectedTab = Tab.Favorite
                    }
                    BottomBarItem(
                        icon = Icons.Default.Person,
                        text = "Profile",
                        isSelected = selectedTab == Tab.Profile
                    ) {
                        selectedTab = Tab.Profile
                    }
                }
            }
        }
    ){
        when (selectedTab) {
            Tab.Movies -> {
                if (!isHomeNavigationDone) {
                    navController.navigate(Screen.Home.route)
                    isHomeNavigationDone = true
                }
            }

            Tab.Favorite -> {
                if (userLoggedIn) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = "Favorite Movies",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            )
                        }

                        if (movieList != null) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                itemsIndexed(
                                    movieList!!,
                                    key = { _, movie -> movie.id }
                                ) { index, movie ->
                                    MovieItem(movie, navController)
                                }
                            }
                        }
                    }
                }
                else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "You need to login to access favorite movies.",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                        Button(
                            onClick = {
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(Screen.Login.route)
                                    launchSingleTop = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(Color.Blue),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Login", color = Color.White)
                        }
                    }
                }
            }

            Tab.Profile -> {
                if (!isProfileNavigationDone) {
                    navController.navigate(Screen.Profile.route)
                    isProfileNavigationDone = true
                }
            }
        }
    }
}