package com.example.inventrytracker.View

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.inventrytracker.Model.InventoryItem
import com.example.inventrytracker.Repository.InventoryRepositoryImpl
import com.example.inventrytracker.Repository.userRepoImpl
import com.example.inventrytracker.ViewModel.InventoryViewModel
import com.example.inventrytracker.ViewModel.UserViewModel

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val inventoryViewModel = InventoryViewModel(InventoryRepositoryImpl())
            val userViewModel = UserViewModel(userRepoImpl())
            DashboardScreen(inventoryViewModel, userViewModel)
        }
    }
}

@Composable
fun DashboardScreen(inventoryViewModel: InventoryViewModel, userViewModel: UserViewModel) {
    val items by inventoryViewModel.inventoryItems.collectAsState()
    var newItemName by remember { mutableStateOf("") }
    var newItemQuantity by remember { mutableStateOf("") }
    val context = LocalContext.current
    val activity = context as Activity

    LaunchedEffect(Unit) {
        inventoryViewModel.startListeningForInventory()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Dashboard", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Button(onClick = {
                userViewModel.logOut { success, message ->
                    if (success) {
                        Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
                        context.startActivity(Intent(context, Login::class.java))
                        activity.finish()
                    } else {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }) {
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
                    val item = InventoryItem(name = name, quantity = quantity)
                    inventoryViewModel.addInventoryItem(item) { success ->
                        if (success) {
                            newItemName = ""
                            newItemQuantity = ""
                            Toast.makeText(context, "Item added", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Failed to add item", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Add Item")
        }

        LazyColumn {
            items(items) { item ->
                InventoryItemView(item = item, viewModel = inventoryViewModel)
            }
        }
    }
}

@Composable
fun InventoryItemView(item: InventoryItem, viewModel: InventoryViewModel) {
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
                    viewModel.uploadImageAndUpdateItem(item, it) { success ->
                        if (success) {
                            Toast.makeText(context, "Image uploaded", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Image upload failed", Toast.LENGTH_SHORT).show()
                        }
                    }
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
                        viewModel.updateInventoryItem(updatedItem) { success ->
                            if (success) {
                                isEditing = false
                                Toast.makeText(context, "Item updated", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Failed to update item", Toast.LENGTH_SHORT).show()
                            }
                        }
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
                    Button(onClick = {
                        viewModel.deleteInventoryItem(item.id) { success ->
                            if (success) {
                                Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }) {
                        Text("Delete")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { imagePicker.launch("image/*") }) {
                    Text(if (item.imageUrl.isNotBlank()) "Change Image" else "Add Image")
                }
            }
        }
    }
}
