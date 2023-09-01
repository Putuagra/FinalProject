package com.example.finalproject.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.finalproject.Screen
import com.example.finalproject.ui.data.Movie
import com.example.finalproject.ui.data.MovieResponse
import com.example.finalproject.ui.data.authorizationHeader
import com.example.finalproject.ui.data.movieApiService
import com.example.finalproject.ui.viewModel.AuthViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class Tab {
    Movies,
    Favorite,
    Profile
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreen(navController: NavController, authViewModel: AuthViewModel) {
    var selectedTab by remember { mutableStateOf(Tab.Movies) }
    var isProfileNavigationDone by remember { mutableStateOf(false) }
    var isFavoriteNavigationDone by remember { mutableStateOf(false) }
    val userLoggedIn = authViewModel.userLoggedIn.value
    val movieNowPlayingList = remember { mutableStateOf(emptyList<Movie>()) }

    val movieTopRatedList = remember { mutableStateOf(emptyList<Movie>()) }

    val moviePopularList = remember { mutableStateOf(emptyList<Movie>()) }

    movieApiService.getNowPlayingMovies(authorizationHeader).enqueue(object :
        Callback<MovieResponse> {
        override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
            if (response.isSuccessful) {
                val movieResponse: MovieResponse? = response.body()
                val nowPlayingMovies: List<Movie> = movieResponse?.results ?: emptyList()

                movieNowPlayingList.value = nowPlayingMovies
            } else {
                Log.e("MovieScreen", "API call failed: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
            Log.e("MovieScreen", "Error fetching movies: ${t.message}", t)
        }
    })

    movieApiService.getTopRatedMovies(authorizationHeader)
        .enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    val movieResponse: MovieResponse? = response.body()
                    val nowPlayingMovies: List<Movie> = movieResponse?.results ?: emptyList()

                    movieTopRatedList.value = nowPlayingMovies
                } else {
                    Log.e("MovieScreen", "API call failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.e("MovieScreen", "Error fetching movies: ${t.message}", t)
            }
        })

    movieApiService.getPopularMovies(authorizationHeader)
        .enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    val movieResponse: MovieResponse? = response.body()
                    val nowPlayingMovies: List<Movie> = movieResponse?.results ?: emptyList()

                    moviePopularList.value = nowPlayingMovies
                } else {
                    Log.e("MovieScreen", "API call failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.e("MovieScreen", "Error fetching movies: ${t.message}", t)
            }
        })

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (userLoggedIn) "Welcome, ${authViewModel.loggedInUsername.value}" else "Movie App",
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                )
            )
        },
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                Tab.Movies -> {
                    MovieCategorySection("Now Playing Movies", movieNowPlayingList.value, navController)
                    MovieCategorySection("Top Rated Movies", movieTopRatedList.value, navController)
                    MovieCategorySection("Popular Movies", moviePopularList.value, navController)
                }

                Tab.Favorite -> {
                    if (!isFavoriteNavigationDone) {
                        navController.navigate(Screen.Favorite.route)
                        isFavoriteNavigationDone = true
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
}

@Composable
fun MovieCategorySection(title: String, movies: List<Movie>, navController: NavController) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp)
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(movies) { movie ->
                MovieItem(movie, navController)
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, navController: NavController) {
    Column(
        modifier = Modifier
            .width(200.dp)
            .height(350.dp)
            .clickable {
                navController.navigate(Screen.Detail.route.replace("{itemId}", movie.id.toString()))
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = "https://image.tmdb.org/t/p/w500${movie.poster_path}"),
            contentDescription = "Movie Poster",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(300.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = movie.title,
            style = TextStyle(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun BottomBarItem(
    icon: ImageVector,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        androidx.compose.material3.Icon(
            imageVector = icon,
            contentDescription = text,
            tint = if (isSelected) Color.Blue else Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            color = if (isSelected) Color.Blue else Color.Gray,
            fontWeight = FontWeight.Bold
        )
    }
}


//@Preview
//@Composable
//fun PreviewMovieDbScreen() {
//    MovieScreen(navController = rememberNavController())
//}
