package com.youllbecold.trustme

import android.app.Application
import com.youllbecold.trustme.di.appModule
import com.youllbecold.trustme.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Application class.
 */
class App : Application(){
    override fun onCreate() {
        super.onCreate()

        // Setup DI
        startKoin{
            if (BuildConfig.DEBUG) {
                androidLogger(Level.DEBUG)
            }
            androidContext(this@App)
            modules(appModule, uiModule)
        }
    }
}
