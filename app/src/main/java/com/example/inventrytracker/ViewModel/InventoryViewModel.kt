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

    fun addItemWithImage(name: String, quantity: Int, image: ByteArray, callback: (Boolean, String?) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val newItem = InventoryItem(name = name, quantity = quantity, userId = userId)
        
        // 1. Add item to get the ID
        repository.addInventoryItem(newItem) { success ->
            if (success) {
                // 2. Upload image
                repository.uploadImage(image) { uploadSuccess, imageUrl, errorMessage ->
                    if (uploadSuccess && imageUrl != null) {
                        // 3. Update item with image URL
                        val updatedItem = newItem.copy(imageUrl = imageUrl)
                        repository.updateInventoryItem(updatedItem) { ok ->
                            callback(ok, if (ok) null else "Failed to update item with URL")
                        }
                    } else {
                        callback(false, errorMessage ?: "Upload failed")
                    }
                }
            } else {
                callback(false, "Failed to create item")
            }
        }
    }

    fun uploadImageAndUpdateItem(item: InventoryItem, image: ByteArray, callback: (Boolean, String?) -> Unit) {
        repository.uploadImage(image) { success, imageUrl, errorMessage ->
            if (success && imageUrl != null) {
                val updatedItem = item.copy(imageUrl = imageUrl)
                updateInventoryItem(updatedItem) { ok ->
                    callback(ok, if (ok) null else "Failed to update item with URL")
                }
            } else {
                callback(false, errorMessage ?: "Upload failed")
            }
        }
    }
}
