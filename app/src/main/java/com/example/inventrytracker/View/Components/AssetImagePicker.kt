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
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Color

@Composable
fun AssetImagePicker(
    context: Context, 
    selectedFileName: String? = null,
    onImageSelected: (String, ByteArray) -> Unit
) {
    // List all files in the assets/inventory tracker directory
    val assetManager = context.assets
    val imageFiles = remember { 
        try {
            assetManager.list("inventory tracker")?.toList() ?: emptyList()
        } catch (e: Exception) {
            emptyList<String>()
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = "Select an Image",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        if (imageFiles.isEmpty()) {
            Text("No images found in assets/inventory tracker", modifier = Modifier.padding(16.dp))
        } else {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(imageFiles) { fileName ->
                    val isSelected = fileName == selectedFileName
                    val bitmap = remember(fileName) {
                        try {
                            val bytes = assetManager.open("inventory tracker/$fileName").use { it.readBytes() }
                            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        } catch (e: Exception) {
                            null
                        }
                    }

                    if (bitmap != null) {
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(if (isSelected) 8.dp else 4.dp),
                            border = if (isSelected) BorderStroke(3.dp, androidx.compose.ui.graphics.Color.Blue) else null,
                            modifier = Modifier
                                .size(100.dp)
                                .padding(2.dp)
                                .clickable { 
                                    try {
                                        val bytes = assetManager.open("inventory tracker/$fileName").use { it.readBytes() }
                                        onImageSelected(fileName, bytes)
                                    } catch (e: Exception) {}
                                }
                        ) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = fileName,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}
