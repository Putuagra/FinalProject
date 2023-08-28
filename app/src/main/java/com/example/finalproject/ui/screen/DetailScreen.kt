package com.example.finalproject.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.finalproject.Screen
import com.example.finalproject.ui.data.Movie
import com.example.finalproject.ui.data.MovieFavorite
import com.example.finalproject.ui.data.MyApplication
import com.example.finalproject.ui.data.authorizationHeader
import com.example.finalproject.ui.data.movieApiService
import com.example.finalproject.ui.viewModel.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailScreen(
    navController: NavController,
    itemId: String,
    authViewModel: AuthViewModel
) {
    val userLoggedIn = authViewModel.userLoggedIn.value
    val context = LocalContext.current
    val application = context.applicationContext as MyApplication
    val database = application.database
    val movieFavDao = database.movieFavoriteDao()

    var movieList by remember { mutableStateOf<List<Movie>?>(null) }

    val movieDetail = remember {
        mutableStateOf<Movie?>(null)
    }

    LaunchedEffect(itemId) {
        val response = movieApiService.getMovieDetail(
            authorizationHeader,
            itemId
        )
        movieDetail.value = response
    }
    LaunchedEffect(key1 = true) {
        CoroutineScope(Dispatchers.IO).launch {
            val movieFavList = movieFavDao.getAllByUserId(authViewModel.loggedInUserId.value)

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

    val movie = movieDetail.value

    movie?.let { movie ->
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    title = { Text(text = "Detail") },
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Home.route) {
                                    inclusive = true
                                }
                            }
                        }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                        }
                    },
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 70.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = "https://image.tmdb.org/t/p/w500${movie.poster_path}"),
                    contentDescription = "Movie Poster",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .height(400.dp)
                        .width(300.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = movie.title,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    )
                    {
                        Text(
                            text = "Overview",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        )
                        Text(
                            text = movie.overview,
                            style = TextStyle(fontSize = 16.sp),
                            textAlign = TextAlign.Justify
                        )
                        Text(
                            text = "Genre",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        )
                        Text(
                            text = movie.genres?.joinToString(", ") { it.name }
                                ?: "No genre available",
                            style = TextStyle(fontSize = 16.sp),
                            textAlign = TextAlign.Justify
                        )
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Vote Average: ${movie.vote_average}",
                            style = TextStyle(fontSize = 16.sp)
                        )

                        Text(
                            text = "Vote Count: ${movie.vote_count}",
                            style = TextStyle(fontSize = 16.sp)
                        )
                    }
                }
                if (userLoggedIn) {
                    // check if movie is in favorite list
                    val isMovieInFavoriteList = movieList?.any { movieFav ->
                        movieFav.id == movie.id
                    } ?: false

                    if (isMovieInFavoriteList) {
                        Text(
                            text = "This movie is in your favorite list",
                            color = Color.Red
                        )
                        Button(
                            onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    val account = userLoggedIn

                                    account?.let {
                                        val movieFavEntity = MovieFavorite(
                                            movie_id = movie.id,
                                            title = movie.title,
                                            poster_path = movie.poster_path,
                                            overview = movie.overview,
                                            release_date = "",
                                            user_id = authViewModel.loggedInUserId.value
                                        )
                                        movieFavDao.deleteFavoriteById(
                                            movieId = movie.id,
                                            userId = authViewModel.loggedInUserId.value
                                        )

                                        withContext(Dispatchers.Main) {
                                            movieList = movieList?.filter { movieFav ->
                                                movieFav.id != movie.id
                                            }
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            colors = ButtonDefaults.buttonColors(
                                Color.Gray
                            ),
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Remove from Favorite", color = Color.White)
                        }
                    } else {
                        Button(
                            onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    val account = userLoggedIn

                                    account?.let {
                                        val movieFavEntity = MovieFavorite(
                                            movie_id = movie.id,
                                            title = movie.title,
                                            poster_path = movie.poster_path,
                                            overview = movie.overview,
                                            release_date = "",
                                            user_id = authViewModel.loggedInUserId.value
                                        )

                                        movieFavDao.insertMovieFav(movieFavEntity)

                                        withContext(Dispatchers.Main) {
                                            val movieFavList =
                                                movieFavDao.getAllByUserId(authViewModel.loggedInUserId.value)

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
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            colors = ButtonDefaults.buttonColors(
                                Color.Red
                            ),
                        ) {
                            Icon(imageVector = Icons.Default.Favorite, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Add to Favorite", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}