package com.example.inventrytracker.View

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inventrytracker.Model.User
import com.example.inventrytracker.R
import com.example.inventrytracker.ViewModel.UserViewModel

/* Inventory Theme Colors */
val InventoryGreen = Color(0xFF2E7D32)
val LightGray = Color(0xFFF1F4F3)

@Composable
fun StoreRegistrationScreen(
    userViewModel: UserViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current

    var ownerName by remember { mutableStateOf("") }
    var storeEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var securityQuestion by remember { mutableStateOf("") }
    var securityAnswer by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var termsAccepted by remember { mutableStateOf(false) }

    StoreRegistrationBody(
        ownerName = ownerName,
        onOwnerNameChange = { ownerName = it },
        storeEmail = storeEmail,
        onStoreEmailChange = { storeEmail = it },
        password = password,
        onPasswordChange = { password = it },
        securityQuestion = securityQuestion,
        onSecurityQuestionChange = { securityQuestion = it },
        securityAnswer = securityAnswer,
        onSecurityAnswerChange = { securityAnswer = it },
        passwordVisible = passwordVisible,
        onPasswordVisibilityChange = { passwordVisible = !passwordVisible },
        termsAccepted = termsAccepted,
        onTermsAcceptedChange = { termsAccepted = it },
        onRegisterClick = {
            if (!termsAccepted) {
                Toast.makeText(context, "Accept terms first", Toast.LENGTH_SHORT).show()
                return@StoreRegistrationBody
            }
            if (ownerName.isBlank() || storeEmail.isBlank() || password.isBlank() || securityQuestion.isBlank() || securityAnswer.isBlank()) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@StoreRegistrationBody
            }

            userViewModel.RegisterUser(ownerName, storeEmail, password) { success, msg, userId ->
                if (success) {
                    val user = User(
                        userId = userId,
                        fullName = ownerName,
                        email = storeEmail,
                        securityQuestion = securityQuestion,
                        securityAnswer = securityAnswer
                    )
                    userViewModel.AddUserToDataBase(userId, user) { ok, message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        if (ok) {
                            onRegisterSuccess()
                        }
                    }
                } else {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
            }
        },
        onLoginClick = onNavigateToLogin
    )
}

@Composable
fun StoreRegistrationBody(
    ownerName: String,
    onOwnerNameChange: (String) -> Unit,
    storeEmail: String,
    onStoreEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    securityQuestion: String,
    onSecurityQuestionChange: (String) -> Unit,
    securityAnswer: String,
    onSecurityAnswerChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityChange: () -> Unit,
    termsAccepted: Boolean,
    onTermsAcceptedChange: (Boolean) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
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
                onValueChange = onOwnerNameChange,
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
                onValueChange = onStoreEmailChange,
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
                onValueChange = onPasswordChange,
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = onPasswordVisibilityChange) {
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

            // Security Question
            Text("Security Question", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = securityQuestion,
                onValueChange = onSecurityQuestionChange,
                placeholder = { Text("e.g., What is your mother\'s maiden name?") },
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

            // Security Answer
            Text("Security Answer", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = securityAnswer,
                onValueChange = onSecurityAnswerChange,
                placeholder = { Text("Enter your answer") },
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
                    onCheckedChange = onTermsAcceptedChange,
                    colors = CheckboxDefaults.colors(checkedColor = InventoryGreen)
                )
                Text("I agree to Terms & Store Policy", fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onRegisterClick,
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
                    .clickable(onClick = onLoginClick)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStoreRegistration() {
    StoreRegistrationBody(
        ownerName = "",
        onOwnerNameChange = {},
        storeEmail = "",
        onStoreEmailChange = {},
        password = "",
        onPasswordChange = {},
        securityQuestion = "",
        onSecurityQuestionChange = {},
        securityAnswer = "",
        onSecurityAnswerChange = {},
        passwordVisible = false,
        onPasswordVisibilityChange = {},
        termsAccepted = false,
        onTermsAcceptedChange = {},
        onRegisterClick = {},
        onLoginClick = {}
    )
}
