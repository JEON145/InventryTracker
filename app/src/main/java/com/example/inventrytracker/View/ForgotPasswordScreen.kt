package com.example.inventrytracker.View

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inventrytracker.ViewModel.UserViewModel

enum class ForgotPasswordStep {
    EnterEmail,
    AnswerQuestion,
    ResetSent
}

@Composable
fun ForgotPasswordScreen(
    userViewModel: UserViewModel,
    onPasswordReset: () -> Unit
) {
    val context = LocalContext.current
    var step by remember { mutableStateOf(ForgotPasswordStep.EnterEmail) }
    var email by remember { mutableStateOf("") }
    var securityQuestion by remember { mutableStateOf("") }
    var securityAnswer by remember { mutableStateOf("") }
    var userAnswer by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Forgot Password", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        when (step) {
            ForgotPasswordStep.EnterEmail -> {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Enter your email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (email.isNotBlank()) {
                            userViewModel.getUserById(email) { success, message, user ->
                                if (success && user != null) {
                                    securityQuestion = user.securityQuestion
                                    securityAnswer = user.securityAnswer
                                    step = ForgotPasswordStep.AnswerQuestion
                                } else {
                                    Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Next")
                }
            }
            ForgotPasswordStep.AnswerQuestion -> {
                Text(securityQuestion)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = userAnswer,
                    onValueChange = { userAnswer = it },
                    label = { Text("Your Answer") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (userAnswer.equals(securityAnswer, ignoreCase = true)) {
                            userViewModel.forgetPassword(email) { success, message ->
                                if (success) {
                                    step = ForgotPasswordStep.ResetSent
                                } else {
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Incorrect answer", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Submit")
                }
            }
            ForgotPasswordStep.ResetSent -> {
                Text("A password reset link has been sent to your email.")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onPasswordReset) {
                    Text("Back to Login")
                }
            }
        }
    }
}
