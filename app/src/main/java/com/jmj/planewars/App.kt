package com.jmj.planewars

import android.app.Application

class App : Application() {
    companion object {
        private lateinit var instance: App
        fun getInstance()= instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}