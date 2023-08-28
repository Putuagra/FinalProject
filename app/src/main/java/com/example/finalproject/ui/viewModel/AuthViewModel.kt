package com.example.finalproject.ui.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.finalproject.Screen
import com.example.finalproject.ui.data.UserDao
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val _userLoggedIn = mutableStateOf(false)
    private val _loggedInUsername = mutableStateOf("")
    private val _loggedInEmail = mutableStateOf("")
    private val _loginError = mutableStateOf(false)
    private val _loggedInUserId = mutableStateOf(0)
    val loginError: State<Boolean> = _loginError
    val userLoggedIn: State<Boolean> = _userLoggedIn
    val loggedInUsername: State<String?> = _loggedInUsername
    val loggedInEmail: State<String?> = _loggedInEmail
    val loggedInUserId: State<Int> = _loggedInUserId

    fun logoutUser(navController: NavController) {
        _userLoggedIn.value = false
        navController.navigate(Screen.Home.route)
    }

    fun loginUser(email: String, password: String, userDao: UserDao) {
        viewModelScope.launch {
            val account = userDao.getAccountByEmailAndPassword(email, password)
            if (account != null) {
                _userLoggedIn.value = true
                _loggedInUserId.value = account.id
                _loggedInUsername.value = account.name
                _loggedInEmail.value = account.email
            }else {
                _loginError.value = true
            }
        }
    }
}