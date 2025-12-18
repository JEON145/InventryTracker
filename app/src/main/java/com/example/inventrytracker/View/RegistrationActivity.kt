package com.example.inventrytracker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.inventrytracker.Model.User
import com.example.inventrytracker.Repository.userRepoImpl
import com.example.inventrytracker.View.Login
import com.example.inventrytracker.ViewModel.UserViewModel
import kotlin.jvm.java

/* Inventory Theme Colors */
val InventoryGreen = Color(0xFF2E7D32)
val LightGray = Color(0xFFF1F4F3)

class StoreRegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StoreRegistrationBody()
        }
    }
}

@Composable
fun StoreRegistrationBody() {

    val userViewModel = remember { UserViewModel(userRepoImpl()) }

    var ownerName by remember { mutableStateOf("") }
    var storeEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var termsAccepted by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as Activity
    val sharedPreference = context.getSharedPreferences("User", Context.MODE_PRIVATE)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Image(
            painter = painterResource(id = R.drawable.img), // 🔁 replace image
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {

            Text(
                text = "Register Store Account",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = InventoryGreen,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Text(
                text = "Manage your store inventory efficiently",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Owner Name
            Text("Store Owner Name", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = ownerName,
                onValueChange = { ownerName = it },
                placeholder = { Text("Enter owner name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = LightGray,
                    unfocusedContainerColor = LightGray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Email
            Text("Store Email", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = storeEmail,
                onValueChange = { storeEmail = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                placeholder = { Text("store@email.com") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = LightGray,
                    unfocusedContainerColor = LightGray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password
            Text("Password", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(
                                if (passwordVisible)
                                    R.drawable.baseline_visibility_24
                                else
                                    R.drawable.baseline_visibility_off_24
                            ),
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = LightGray,
                    unfocusedContainerColor = LightGray,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = termsAccepted,
                    onCheckedChange = { termsAccepted = it },
                    colors = CheckboxDefaults.colors(checkedColor = InventoryGreen)
                )
                Text("I agree to Terms & Store Policy", fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = {
                    if (!termsAccepted) {
                        Toast.makeText(context, "Accept terms first", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val localEmail = sharedPreference.getString("email", "")
                    if (localEmail == storeEmail) {
                        Toast.makeText(context, "Email already exists", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    userViewModel.RegisterUser(ownerName, storeEmail, password) { success, msg, userId ->
                        if (success) {
                            val user = User(
                                userId = userId,
                                fullName = ownerName,
                                email = storeEmail
                            )
                            userViewModel.AddUserToDataBase(userId, user) { ok, message ->
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                if (ok) activity.finish()
                            }
                        } else {
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = InventoryGreen)
            ) {
                Text("Register Store", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                buildAnnotatedString {
                    append("Already registered? ")
                    withStyle(
                        SpanStyle(
                            color = InventoryGreen,
                            fontWeight = FontWeight.Medium
                        )
                    ) {
                        append("Login")
                    }
                },
                fontSize = 13.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        context.startActivity(Intent(context, Login::class.java))
                        activity.finish()
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStoreRegistration() {
    StoreRegistrationBody()
}
