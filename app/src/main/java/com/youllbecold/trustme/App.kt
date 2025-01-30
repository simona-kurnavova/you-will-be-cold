package com.youllbecold.trustme

import android.app.Application
import com.youllbecold.trustme.di.appModule
import com.youllbecold.trustme.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Application class.
 */
class App : Application(){
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger()
            androidContext(this@App)
            modules(appModule, uiModule)
        }
    }
}