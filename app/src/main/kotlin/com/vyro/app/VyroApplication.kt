package com.vyro.app

import android.app.Application
import android.util.Log

class VyroApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.i("VYRO", "VYRO application initialized — on-device video intelligence")
    }
}