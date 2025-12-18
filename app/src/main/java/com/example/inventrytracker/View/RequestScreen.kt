package com.example.inventrytracker.View


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RequestsScreen() {
    var selectedTab by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Requests",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                label = { Text("Received") },
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                label = { Text("Sent") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            if (selectedTab == 0) {
                RequestCard(
                    from = "Green Market",
                    product = "White Rice 5kg",
                    quantity = "10 bags",
                    status = "Pending",
                    isReceived = true
                )
                RequestCard(
                    from = "City Grocers",
                    product = "Cooking Oil 1L",
                    quantity = "5 bottles",
                    status = "Accepted",
                    isReceived = true
                )
            } else {
                RequestCard(
                    from = "Fresh Valley",
                    product = "Tomatoes",
                    quantity = "15 kg",
                    status = "Pending",
                    isReceived = false
                )
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun RequestCard(from: String, product: String, quantity: String, status: String, isReceived: Boolean) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isReceived) "From: $from" else "To: $from",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                    Text(product, color = Color(0xFF2196F3), fontSize = 14.sp)
                    Text("Qty: $quantity", color = Color.Gray, fontSize = 12.sp)
                }

                Box(
                    modifier = Modifier
                        .background(
                            if (status == "Pending") Color(0xFFFFF9C4) else Color(0xFFC8E6C9),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        status,
                        color = if (status == "Pending") Color(0xFFF57F17) else Color(0xFF2E7D32),
                        fontSize = 12.sp
                    )
                }
            }

            if (status == "Pending" && isReceived) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        )
                    ) {
                        Text("Accept")
                    }
                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Decline")
                    }
                }
            }
        }
    }
}
