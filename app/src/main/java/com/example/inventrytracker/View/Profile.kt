package com.example.inventrytracker.View


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inventrytracker.repository.ProfileRepoImpl
import com.example.inventrytracker.ui.theme.InventryTrackerTheme
import com.example.inventrytracker.viewmodel.ProfileViewModel


class Profile : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InventryTrackerTheme {
                ProfileBody()
            }
        }
    }
}

@Composable
fun ProfileBody() {

    val viewModel = remember { ProfileViewModel(ProfileRepoImpl()) }

    val email = remember { viewModel.getEmail() }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Profile",
                fontSize = 26.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Email:",
                fontSize = 16.sp
            )

            Text(
                text = email,
                fontSize = 18.sp
            )
        }
    }
}
