package com.learning.orderfoodappsch3

import android.app.Application
import com.learning.orderfoodappsch3.data.database.AppDatabase

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.getInstance(this)
    }
}