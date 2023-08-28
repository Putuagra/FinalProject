@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.finalproject.ui.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finalproject.R
import com.example.finalproject.Screen
import com.example.finalproject.ui.data.UserDao
import com.example.finalproject.ui.viewModel.AuthViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun Login(navController: NavController, userDao: UserDao, authViewModel: AuthViewModel) {
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var isLoginEnabled by remember { mutableStateOf(false) }

    fun validateInput() {
        isLoginEnabled = email.isNotBlank() && password.isNotBlank()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onSecondary
                ),
                title = { Text(text = "Login", color = Color.White) },
                modifier = Modifier
                    .background(Color.Blue)
            )
        }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.login),
                contentDescription = null,
                modifier = Modifier
                    .size(350.dp)
                    .padding(8.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
            )
            OutlinedTextField(
                value = email,
                onValueChange = { newEmail ->
                    email = newEmail
                    validateInput()
                },
                label = { Text(text = "Email") },
                textStyle = TextStyle(fontSize = 18.sp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { newPassword ->
                    password = newPassword
                    validateInput()
                },
                label = { Text(text = "Password") },
                textStyle = TextStyle(fontSize = 18.sp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    authViewModel.loginUser(email, password, userDao)
                },
                enabled = isLoginEnabled,
                colors = ButtonDefaults.buttonColors(
                    if (isLoginEnabled) Color.Blue else Color.Gray
                ),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Login", color = Color.White)
            }
            val context = LocalContext.current
            LaunchedEffect(authViewModel.userLoggedIn.value, authViewModel.loginError.value) {
                if (authViewModel.userLoggedIn.value) {
                    Toast.makeText(
                        context,
                        "Login successful!",
                        Toast.LENGTH_SHORT
                    ).show()

                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route)
                        launchSingleTop = true
                    }
                }else if (authViewModel.loginError.value) {
                    Toast.makeText(
                        context,
                        "Login failed. Check email and password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            androidx.compose.material3.TextButton(
                onClick = {
                    navController.navigate(Screen.Register.route) {
                        popUpTo(Screen.Register.route)
                        launchSingleTop = true
                    }

                }) {
                Text(
                    text = "Create An Account",
                    letterSpacing = 1.sp,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

//@ExperimentalMaterial3Api
//@Preview(showBackground = true)
//@Composable
//fun LoginPreview() {
//    Login(navController = rememberNavController())
//}
