package com.example.finalproject.ui.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

data class MovieResponse(
    val results: List<Movie>?
)

data class Genre(
    val id: Int,
    val name: String
)

data class Movie(
    val id: Int,
    val title: String,
    val poster_path: String,
    val overview: String,
    val vote_average: Double,
    val vote_count: Int,
    val genres: List<Genre>?
)

interface MovieApiService {
    @GET("movie/now_playing?language=en-US&page=1")
    suspend fun getNowPlayingMovies(
        @Header("Authorization") authorization: String
    ): MovieResponse

    @GET("movie/top_rated?language=en-US&page=1")
    suspend fun getTopRatedMovies(
        @Header("Authorization") authorization: String
    ): MovieResponse

    @GET("movie/popular?language=en-US&page=1")
    suspend fun getPopularMovies(
        @Header("Authorization") authorization: String
    ): MovieResponse

    @GET("movie/{movie_id}?language=en-US")
    suspend fun getMovieDetail(
        @Header("Authorization") authorization: String,
        @Path("movie_id") movieId: String
    ): Movie
}

object RetrofitInstance {
    private const val BASE_URL = "https://api.themoviedb.org/3/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder().build())
        .build()
}

val movieApiService: MovieApiService = RetrofitInstance.retrofit.create(MovieApiService::class.java)

const val authorizationHeader = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2YTQyOWYyNGRhMTZlYTExZGQ4MWM2NzY5MTNmY2U3OSIsInN1YiI6IjY0ZTViZWM0MWZlYWMxMDBmZTViNDEwNCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.1w-jElhx7gAiIMpGhREOvJ7g-7zrRgnRmxa9ybPcPhw"
