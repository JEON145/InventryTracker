package com.example.inventrytracker.Repository

import com.example.inventrytracker.Model.InventoryItem

interface InventoryRepository {
    fun getInventoryItems(userId: String, callback: (List<InventoryItem>) -> Unit)
    fun addInventoryItem(item: InventoryItem, callback: (Boolean) -> Unit)
    fun updateInventoryItem(item: InventoryItem, callback: (Boolean) -> Unit)
    fun deleteInventoryItem(itemId: String, callback: (Boolean) -> Unit)
    fun uploadImage(image: ByteArray, callback: (Boolean, String?, String?) -> Unit)
}
