package com.example.inventrytracker.View

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.inventrytracker.Model.InventoryItem
import com.example.inventrytracker.R
import com.example.inventrytracker.ViewModel.InventoryViewModel
import com.example.inventrytracker.ViewModel.ViewModelFactory
import com.example.inventrytracker.View.Components.AssetImagePicker
import androidx.compose.material3.Button
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun InventryScreen(inventoryViewModel: InventoryViewModel) {
    val items by inventoryViewModel.inventoryItems.collectAsState()

    LaunchedEffect(Unit) {
        inventoryViewModel.startListeningForInventory()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Your Inventory",
            color = Color.Black,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(bottom = 80.dp), // To avoid bottom nav
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items) { item ->
                InventoryGridItem(item) { imageBytes ->
                    inventoryViewModel.uploadImageAndUpdateItem(item, imageBytes) { success, error ->
                        // Optional: Show toast
                    }
                }
            }
        }
    }
}

@Composable
fun InventoryGridItem(item: InventoryItem, onImageSelected: (ByteArray) -> Unit) {
    val context = LocalContext.current
    var isPickingImage by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column {
            AsyncImage(
                model = item.imageUrl.ifEmpty { R.drawable.img },
                contentDescription = item.name,
                placeholder = coil.compose.rememberAsyncImagePainter(R.drawable.img),
                error = coil.compose.rememberAsyncImagePainter(R.drawable.img),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Qty: ${item.quantity}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { isPickingImage = !isPickingImage },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = if (item.imageUrl.isEmpty()) "Add Image" else "Change", fontSize = 12.sp)
                }

                if (isPickingImage) {
                    AssetImagePicker(context = context) { imageBytes ->
                        onImageSelected(imageBytes)
                        isPickingImage = false
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InventryScreenPreview() {
    val context = LocalContext.current
    InventryScreen(viewModel(factory = ViewModelFactory(context.applicationContext as Application)))
}
