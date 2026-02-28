package com.example.inventrytracker

import android.app.Application
import com.example.inventrytracker.Repository.InventoryRepository
import com.example.inventrytracker.Repository.InventoryRepositoryImpl
import com.example.inventrytracker.Repository.UserRepo
import com.example.inventrytracker.Repository.userRepoImpl
import com.example.inventrytracker.Utils.CloudinaryConfig
import com.google.firebase.FirebaseApp

class InventryTrackerApp : Application() {

    // Create a single, shared instance of the repositories for the entire app.
    // This is the core fix for the ANR.
    val userRepo: UserRepo by lazy {
        userRepoImpl()
    }
    val inventoryRepository: InventoryRepository by lazy {
        InventoryRepositoryImpl()
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        CloudinaryConfig.initialize(this)
    }
}
