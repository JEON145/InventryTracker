package com.example.inventrytracker.Repository

import com.example.inventrytracker.Model.InventoryItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class InventoryRepositoryImpl : InventoryRepository {

    private val database = FirebaseDatabase.getInstance().getReference("inventory")
    private val storage = FirebaseStorage.getInstance().reference

    override fun getInventoryItems(userId: String, callback: (List<InventoryItem>) -> Unit) {
        database.orderByChild("userId").equalTo(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = mutableListOf<InventoryItem>()
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(InventoryItem::class.java)
                    item?.let { items.add(it) }
                }
                callback(items)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    override fun addInventoryItem(item: InventoryItem, callback: (Boolean) -> Unit) {
        val itemId = database.push().key ?: return
        item.id = itemId
        database.child(itemId).setValue(item).addOnCompleteListener { task ->
            callback(task.isSuccessful)
        }
    }

    override fun updateInventoryItem(item: InventoryItem, callback: (Boolean) -> Unit) {
        database.child(item.id).setValue(item).addOnCompleteListener { task ->
            callback(task.isSuccessful)
        }
    }

    override fun deleteInventoryItem(itemId: String, callback: (Boolean) -> Unit) {
        database.child(itemId).removeValue().addOnCompleteListener { task ->
            callback(task.isSuccessful)
        }
    }

    override fun uploadImage(image: ByteArray, callback: (Boolean, String?) -> Unit) {
        val imageId = UUID.randomUUID().toString()
        val imageRef = storage.child("images/$imageId")

        imageRef.putBytes(image).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                imageRef.downloadUrl.addOnCompleteListener { urlTask ->
                    if (urlTask.isSuccessful) {
                        callback(true, urlTask.result.toString())
                    } else {
                        callback(false, null)
                    }
                }
            } else {
                callback(false, null)
            }
        }
    }
}
