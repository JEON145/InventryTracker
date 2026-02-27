package com.example.inventrytracker.Model

data class User(
    val userId:String="",
    val email: String="",
    val fullName: String = "",
    val securityQuestion: String = "",
    val securityAnswer: String = ""
){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "userId" to userId,
            "email" to email,
            "fullName" to fullName,
            "securityQuestion" to securityQuestion,
            "securityAnswer" to securityAnswer
        )
    }
}
