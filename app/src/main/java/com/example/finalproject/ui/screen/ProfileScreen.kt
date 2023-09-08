package com.example.finalproject.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.finalproject.R
import com.example.finalproject.Screen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    isLoggedIn: Boolean,
    accountName: String,
    accountEmail: String,
    onLoggedChanges: () -> Unit
) {
    val sharedPreferences =
        LocalContext.current.getSharedPreferences("session", Context.MODE_PRIVATE)
    var selectedTab by remember { mutableStateOf(Tab.Profile) }
    var isHomeNavigationDone by remember { mutableStateOf(false) }
    var isFavoriteNavigationDone by remember { mutableStateOf(false) }
    val loggedInUser =
        remember(accountName, accountEmail) {
            LoggedInUser(
                username = accountName,
                email = accountEmail
            )
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
    ) {
        when (selectedTab) {
            Tab.Movies -> {
                if (!isHomeNavigationDone) {
                    navController.navigate(Screen.Home.route)
                    isHomeNavigationDone = true
                }
            }

            Tab.Favorite -> {
                if (!isFavoriteNavigationDone) {
                    navController.navigate(Screen.Favorite.route)
                    isFavoriteNavigationDone = true
                }
            }

            Tab.Profile -> {
                if (isLoggedIn) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.profile),
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ProfileItem(Icons.Default.Person, "Username", loggedInUser.username)
                        Spacer(modifier = Modifier.height(8.dp))
                        ProfileItem(Icons.Default.Email, "Email", loggedInUser.email)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                val editor = sharedPreferences.edit()
                                editor.putBoolean("isLoggedIn", false)
                                editor.clear()
                                editor.apply()
                                onLoggedChanges()
                                navController.navigate(Screen.Home.route)
                            },
                            colors = ButtonDefaults.buttonColors(Color.Blue),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Logout", color = Color.White)
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "You need to login to access the profile.",
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
        }
    }
}

data class LoggedInUser(val username: String, val email: String)

@Composable
fun ProfileItem(icon: ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = "$label: $value"
        )
    }
}