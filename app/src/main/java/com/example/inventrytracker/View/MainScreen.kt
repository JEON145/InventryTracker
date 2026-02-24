package com.example.inventrytracker.View

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.inventrytracker.Repository.InventoryRepositoryImpl
import com.example.inventrytracker.Repository.userRepoImpl
import com.example.inventrytracker.ViewModel.InventoryViewModel
import com.example.inventrytracker.ViewModel.UserViewModel
import com.example.inventrytracker.ViewModel.ViewModelFactory

sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    object Dashboard : BottomNavItem("dashboard", Icons.Default.Home, "Dashboard")
    object Inventory : BottomNavItem("inventory", Icons.Default.List, "Inventory")
    object Profile : BottomNavItem("profile", Icons.Default.AccountCircle, "Profile")
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val userViewModel: UserViewModel = viewModel(factory = ViewModelFactory(userRepo = userRepoImpl()))
    val inventoryViewModel: InventoryViewModel = viewModel(factory = ViewModelFactory(inventoryRepo = InventoryRepositoryImpl()))

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val items = listOf(
                    BottomNavItem.Dashboard,
                    BottomNavItem.Inventory,
                    BottomNavItem.Profile
                )
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = BottomNavItem.Dashboard.route,
            Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Dashboard.route) { DashboardScreen(inventoryViewModel, userViewModel) }
            composable(BottomNavItem.Inventory.route) { InventryScreen() }
            composable(BottomNavItem.Profile.route) { ProfileScreen() }
        }
    }
}
