package com.example.inventrytracker.ViewModel

import androidx.lifecycle.ViewModel
import com.example.inventrytracker.Model.InventoryItem
import com.example.inventrytracker.Repository.InventoryRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InventoryViewModel(private val repository: InventoryRepository) : ViewModel() {

    private val _inventoryItems = MutableStateFlow<List<InventoryItem>>(emptyList())
    val inventoryItems: StateFlow<List<InventoryItem>> = _inventoryItems.asStateFlow()

    fun startListeningForInventory() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        repository.getInventoryItems(userId) { items ->
            _inventoryItems.value = items
        }
    }

    fun addInventoryItem(item: InventoryItem, callback: (Boolean) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val newItem = item.copy(userId = userId)
        repository.addInventoryItem(newItem, callback)
    }

    fun updateInventoryItem(item: InventoryItem, callback: (Boolean) -> Unit) {
        repository.updateInventoryItem(item, callback)
    }

    fun deleteInventoryItem(itemId: String, callback: (Boolean) -> Unit) {
        repository.deleteInventoryItem(itemId, callback)
    }

    fun uploadImageAndUpdateItem(item: InventoryItem, image: ByteArray, callback: (Boolean) -> Unit) {
        repository.uploadImage(image) { success, imageUrl ->
            if (success && imageUrl != null) {
                val updatedItem = item.copy(imageUrl = imageUrl)
                updateInventoryItem(updatedItem, callback)
            } else {
                callback(false)
            }
        }
    }
}
