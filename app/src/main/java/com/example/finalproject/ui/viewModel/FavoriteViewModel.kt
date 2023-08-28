package com.example.finalproject.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(private val authViewModel: AuthViewModel) : ViewModel() {
    private val _favoriteMovies: MutableMap<String, MutableSet<String>> = mutableMapOf()
    val favoriteStatusFlow: StateFlow<Map<String, Boolean>>
        get() = _favoriteStatusFlow

    private val _favoriteStatusFlow: MutableStateFlow<Map<String, Boolean>> =
        MutableStateFlow(emptyMap())

    init {
        viewModelScope.launch {
            val userEmail = authViewModel.loggedInEmail.value
            if (userEmail != null) {
                _favoriteStatusFlow.value = _favoriteMovies[userEmail]?.associateWith { true }
                    ?: emptyMap()
            }
        }
    }

    fun isMovieFavorite(movieId: String): Boolean {
        val userEmail = authViewModel.loggedInEmail.value
        return userEmail != null && _favoriteMovies[userEmail]?.contains(movieId) ?: false
    }

    fun clearFavoriteStatus() {
        val userEmail = authViewModel.loggedInEmail.value
        if (userEmail != null) {
            _favoriteMovies.remove(userEmail)
        }
    }

    fun toggleFavorite(movieId: String) {
        val userEmail = authViewModel.loggedInEmail.value
        if (userEmail != null) {
            val userFavorites = _favoriteMovies.getOrPut(userEmail) {
                mutableSetOf()
            }
            if (userFavorites.contains(movieId)) {
                userFavorites.remove(movieId)
            } else {
                userFavorites.add(movieId)
            }

            // Mengirimkan perubahan status favorit melalui _favoriteStatusFlow
            _favoriteStatusFlow.value = userFavorites.associateWith { true }
        }
    }
}