package com.example.inventrytracker.Utils

import android.content.Context
import com.cloudinary.android.MediaManager

object CloudinaryConfig {
    private const val CLOUD_NAME = "dfdr4hwh"
    private const val API_KEY = "527637818238182"
    private const val API_SECRET = "JKQWLEAVRlSkVDDo4hm6J-AlqGs"
    const val UPLOAD_PRESET = "ea982fe7-fdb5-44c7-93e3-db6f799d6972"

    fun initialize(context: Context) {
        val config = mapOf(
            "cloud_name" to CLOUD_NAME,
            "api_key" to API_KEY,
            "api_secret" to API_SECRET
        )
        try {
            MediaManager.init(context, config)
        } catch (e: Exception) {
            // MediaManager might already be initialized
        }
    }
}
