package com.example.inventrytracker.Model

import com.google.firebase.database.Exclude

data class InventoryItem(
    var id: String = "",
    val name: String = "",
    val quantity: Int = 0,
    val userId: String = "",
    val imageUrl: String = ""
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "quantity" to quantity,
            "userId" to userId,
            "imageUrl" to imageUrl
        )
    }
}
