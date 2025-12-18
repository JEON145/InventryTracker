package com.example.inventrytracker.ViewModel

import androidx.lifecycle.ViewModel
import com.example.inventrytracker.Model.User
import com.example.inventrytracker.Repository.UserRepo

class UserViewModel(
    private val repository: UserRepo
) : ViewModel() {

    fun loginUser(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        repository.login(email, password) { success, message ->
            if (success) {
                onSuccess()
            } else {
                onError(message)
            }
        }
    }
    fun RegisterUser(
        fullName: String,
        email: String,
        password: String,
        callback: (Boolean, String, String) -> Unit
    ) {
        repository.register(email, password) { success, message ->
            if (success) {
                val userId = email   // simple unique ID
                callback(true, "Registration successful", userId)
            } else {
                callback(false, message, "")
            }
        }
    }

    fun AddUserToDataBase(
        userId: String,
        user: User,
        callback: (Boolean, String) -> Unit
    ) {
        repository.addUserToDatabase(userId, user) { success, message ->
            callback(success, message)
        }
    }
}
fun forgetPassword(
    email: String,
    callback: (Boolean, String) -> Unit
) {
    repository.forgetPassword(email, callback)
}





