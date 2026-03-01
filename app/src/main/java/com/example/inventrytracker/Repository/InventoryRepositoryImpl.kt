package com.example.inventrytracker.Repository

import com.example.inventrytracker.Model.InventoryItem
import com.example.inventrytracker.Utils.CloudinaryConfig
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import java.util.UUID

class InventoryRepositoryImpl : InventoryRepository {

    private val database by lazy {
        FirebaseDatabase.getInstance().getReference("inventory")
    }

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
        val requestId = MediaManager.get().upload(image)
            .unsigned(CloudinaryConfig.UPLOAD_PRESET)
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {
                    // Pre-upload handling
                }

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                    // Progress tracking
                }

                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    val imageUrl = resultData["secure_url"] as? String
                    println("Cloudinary Upload Success: $imageUrl")
                    callback(true, imageUrl)
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    println("Cloudinary Upload Error: ${error.description}")
                    callback(false, null)
                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {
                    // Rescheduling if network fails
                }
            })
            .dispatch()
    }
}
