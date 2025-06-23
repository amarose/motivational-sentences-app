package com.example.motivationalsentencesapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import com.example.motivationalsentencesapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }
}
