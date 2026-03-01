// FILE 4: ProfileScreen.kt
package com.example.inventrytracker.View

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inventrytracker.ViewModel.UserViewModel
import androidx.compose.runtime.*
import com.example.inventrytracker.Model.User
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(userViewModel: UserViewModel, onLogout: () -> Unit) {
    var userProfile by remember { mutableStateOf<User?>(null) }
    val currentUser = FirebaseAuth.getInstance().currentUser
    val email = currentUser?.email ?: ""

    LaunchedEffect(email) {
        if (email.isNotEmpty()) {
            userViewModel.getUserById(email) { success, message, user ->
                if (success) {
                    userProfile = user
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color(0xFFE3F2FD), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(50.dp),
                tint = Color(0xFF2196F3)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = userProfile?.fullName ?: "Loading...",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
 
        Text(
            text = userProfile?.email ?: email,
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                ProfileMenuItemRow(Icons.Default.ShoppingCart, "My Store")
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                ProfileMenuItemRow(Icons.Default.Home, "My Products")
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                ProfileMenuItemRow(Icons.Default.Notifications, "Notifications")
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                ProfileMenuItemRow(Icons.Default.Settings, "Settings")
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                ProfileMenuItemRow(Icons.Default.Info, "Help & Support")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = { onLogout() },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFFF44336)
            )
        ) {
            Icon(Icons.AutoMirrored.Filled.ExitToApp, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout")
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun ProfileMenuItemRow(icon: ImageVector, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF2196F3)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, fontSize = 16.sp)
        }
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}