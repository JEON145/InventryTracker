package com.example.inventrytracker.View.Components

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp

@Composable
fun AssetImagePicker(context: Context, onImageSelected: (ByteArray) -> Unit) {
    // List all files in the assets/inventory tracker directory
    val assetManager = context.assets
    val imageFiles = remember { 
        try {
            assetManager.list("inventory tracker")?.toList() ?: emptyList()
        } catch (e: Exception) {
            emptyList<String>()
        }
    }

    LazyRow(modifier = Modifier.padding(vertical = 8.dp)) {
        items(imageFiles) { fileName ->
            val bitmap = remember(fileName) {
                try {
                    val bytes = assetManager.open("inventory tracker/$fileName").use { it.readBytes() }
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                } catch (e: Exception) {
                    null
                }
            }

            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = fileName,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(4.dp)
                        .clickable { 
                            try {
                                val bytes = assetManager.open("inventory tracker/$fileName").use { it.readBytes() }
                                onImageSelected(bytes)
                            } catch (e: Exception) {}
                        }
                )
            }
        }
    }
}
