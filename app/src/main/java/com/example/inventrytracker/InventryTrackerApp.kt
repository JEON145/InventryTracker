package com.example.inventrytracker

import android.app.Application
import com.google.firebase.FirebaseApp

class InventryTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
