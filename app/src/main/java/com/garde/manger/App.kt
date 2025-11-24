package com.garde.manger

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@GardeApp)
            modules(
                platformModule,  // DatabaseDriverFactory
                dataModule, // Database, DataSources, HttpClient
                productModule,
                addProductModule,
            )
        }
    }
}