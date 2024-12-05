package com.sinjidragon.nurijang

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NurijangApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this.applicationContext
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context

        fun getContext(): Context {
            if (!::context.isInitialized) {
                throw IllegalStateException("Application context has not been initialized.")
            }
            return context
        }
    }
}