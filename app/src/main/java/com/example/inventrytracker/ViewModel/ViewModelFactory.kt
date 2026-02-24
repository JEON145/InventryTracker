package com.example.inventrytracker.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.inventrytracker.Repository.InventoryRepository
import com.example.inventrytracker.Repository.UserRepo

class ViewModelFactory(
    private val userRepo: UserRepo? = null,
    private val inventoryRepo: InventoryRepository? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java) && userRepo != null) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(userRepo) as T
        }
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java) && inventoryRepo != null) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(inventoryRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
