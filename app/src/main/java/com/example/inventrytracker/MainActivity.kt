package com.example.inventrytracker

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inventrytracker.View.ForgotPasswordScreen
import com.example.inventrytracker.View.LoginScreen
import com.example.inventrytracker.View.MainScreen
import com.example.inventrytracker.View.StoreRegistrationScreen
import com.example.inventrytracker.ViewModel.InventoryViewModel
import com.example.inventrytracker.ViewModel.UserViewModel
import com.example.inventrytracker.ViewModel.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val factory = ViewModelFactory(context.applicationContext as Application)
    val userViewModel: UserViewModel = viewModel(factory = factory)
    val inventoryViewModel: InventoryViewModel = viewModel(factory = factory)

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                userViewModel = userViewModel,
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onNavigateToForgotPassword = {
                    navController.navigate("forgot_password")
                }
            )
        }
        composable("register") {
            StoreRegistrationScreen(
                userViewModel = userViewModel,
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
        composable("forgot_password") {
            ForgotPasswordScreen(
                userViewModel = userViewModel,
                onPasswordReset = {
                    navController.popBackStack()
                }
            )
        }
        composable("main") {
            MainScreen(
                userViewModel = userViewModel,
                inventoryViewModel = inventoryViewModel,
                onLogout = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("login") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }
    }
}
