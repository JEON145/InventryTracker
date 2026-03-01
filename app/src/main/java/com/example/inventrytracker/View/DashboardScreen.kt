package com.example.inventrytracker.View

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import com.example.inventrytracker.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.inventrytracker.Model.InventoryItem
import com.example.inventrytracker.ViewModel.InventoryViewModel
import com.example.inventrytracker.ViewModel.UserViewModel

@Composable
fun DashboardScreen(
    inventoryViewModel: InventoryViewModel,
    userViewModel: UserViewModel,
    onLogout: () -> Unit
) {
    val items by inventoryViewModel.inventoryItems.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        inventoryViewModel.startListeningForInventory()
    }

    DashboardBody(
        items = items,
        onLogoutClick = {
            userViewModel.logOut { success, message ->
                if (success) {
                    Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
                    onLogout()
                } else {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        },
        onAddItemClick = { name, quantity ->
            val item = InventoryItem(name = name, quantity = quantity)
            inventoryViewModel.addInventoryItem(item) { success ->
                if (success) {
                    Toast.makeText(context, "Item added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to add item", Toast.LENGTH_SHORT).show()
                }
            }
        },
        onUpdateItemClick = { item ->
            inventoryViewModel.updateInventoryItem(item) { success ->
                if (success) {
                    Toast.makeText(context, "Item updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to update item", Toast.LENGTH_SHORT).show()
                }
            }
        },
        onDeleteItemClick = { itemId ->
            inventoryViewModel.deleteInventoryItem(itemId) { success ->
                if (success) {
                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show()
                }
            }
        },
        onUploadImageClick = { item, imageBytes ->
            inventoryViewModel.uploadImageAndUpdateItem(item, imageBytes) { success ->
                if (success) {
                    Toast.makeText(context, "Image uploaded", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Image upload failed", Toast.LENGTH_SHORT).show()
                }
            }
        },
        onAddItemWithImageClick = { name, quantity, imageBytes ->
            inventoryViewModel.addItemWithImage(name, quantity, imageBytes) { success ->
                if (success) {
                    Toast.makeText(context, "Item and Image added!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to add item with image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )
}

@Composable
fun DashboardBody(
    items: List<InventoryItem>,
    onLogoutClick: () -> Unit,
    onAddItemClick: (String, Int) -> Unit,
    onUpdateItemClick: (InventoryItem) -> Unit,
    onDeleteItemClick: (String) -> Unit,
    onUploadImageClick: (InventoryItem, ByteArray) -> Unit,
    onAddItemWithImageClick: (String, Int, ByteArray) -> Unit
) {
    var newItemName by remember { mutableStateOf("") }
    var newItemQuantity by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Dashboard", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Button(onClick = onLogoutClick) {
                Text("Logout")
            }
        }

        Spacer(modifier = Modifier.padding(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                value = newItemName,
                onValueChange = { newItemName = it },
                label = { Text("Item Name") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            TextField(
                value = newItemQuantity,
                onValueChange = { newItemQuantity = it },
                label = { Text("Quantity") },
                modifier = Modifier.width(100.dp)
            )
        }
        Button(
            onClick = {
                val name = newItemName
                val quantity = newItemQuantity.toIntOrNull() ?: 0
                if (name.isNotBlank()) {
                    onAddItemClick(name, quantity)
                    newItemName = ""
                    newItemQuantity = ""
                }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Add Item")
        }

        Text("Quick Start (Add Item with Sample Photo):", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            val sampleItems: List<Pair<String, Int>> = listOf(
                Pair("Sample 1", com.example.inventrytracker.R.drawable.img),
                Pair("Sample 2", com.example.inventrytracker.R.drawable.img_1)
            )
            sampleItems.forEach { (name, resId) ->
                Button(onClick = {
                    val bitmap = BitmapFactory.decodeResource(context.resources, resId)
                    val out = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                    val imageBytes = out.toByteArray()
                    
                    onAddItemWithImageClick(name, 1, imageBytes)
                }) {
                    Text(name)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(items) { item ->
                InventoryItemView(
                    item = item,
                    onUpdateItemClick = onUpdateItemClick,
                    onDeleteItemClick = onDeleteItemClick,
                    onUploadImageClick = onUploadImageClick
                )
            }
        }
    }
}

@Composable
fun InventoryItemView(
    item: InventoryItem,
    onUpdateItemClick: (InventoryItem) -> Unit,
    onDeleteItemClick: (String) -> Unit,
    onUploadImageClick: (InventoryItem, ByteArray) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableStateOf(item.quantity.toString()) }
    val context = LocalContext.current

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                val inputStream = context.contentResolver.openInputStream(uri)
                val imageBytes = inputStream?.readBytes()
                inputStream?.close()

                imageBytes?.let {
                    onUploadImageClick(item, it)
                }
            }
        }
    )

    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (item.imageUrl.isNotBlank()) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = "Inventory Item Image",
                    placeholder = coil.compose.rememberAsyncImagePainter(R.drawable.img),
                    error = coil.compose.rememberAsyncImagePainter(R.drawable.img),
                    modifier = Modifier.height(150.dp).fillMaxWidth().padding(bottom = 8.dp)
                )
            } else {
                AsyncImage(
                    model = R.drawable.img,
                    contentDescription = "Placeholder Image",
                    modifier = Modifier.height(150.dp).fillMaxWidth().padding(bottom = 8.dp)
                )
            }
            if (isEditing) {
                TextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    label = { Text("Item Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = editedQuantity,
                    onValueChange = { editedQuantity = it },
                    label = { Text("Quantity") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(onClick = {
                        val updatedItem = item.copy(
                            name = editedName,
                            quantity = editedQuantity.toIntOrNull() ?: 0
                        )
                        onUpdateItemClick(updatedItem)
                        isEditing = false
                    }) {
                        Text("Save")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { isEditing = false }) {
                        Text("Cancel")
                    }
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = item.name, modifier = Modifier.weight(1f))
                    Text(text = "Qty: ${item.quantity}")
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = { isEditing = true }) {
                        Text("Edit")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { onDeleteItemClick(item.id) }) {
                        Text("Delete")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                    Button(onClick = { imagePicker.launch("image/*") }) {
                        Text(if (item.imageUrl.isNotBlank()) "Change Image" else "Add Image")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    DashboardBody(
        items = listOf(
            InventoryItem(id = "1", name = "Sample Item 1", quantity = 10, imageUrl = ""),
            InventoryItem(id = "2", name = "Sample Item 2", quantity = 5, imageUrl = "")
        ),
        onLogoutClick = {},
        onAddItemClick = { _, _ -> },
        onUpdateItemClick = {},
        onDeleteItemClick = {},
        onUploadImageClick = { _, _ -> },
        onAddItemWithImageClick = { _, _, _ -> }
    )
}
