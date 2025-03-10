package com.youllbecold.trustme

import android.app.Application
import com.youllbecold.trustme.common.ui.notifications.channel.ChannelInitializer
import com.youllbecold.trustme.di.commonModules
import com.youllbecold.trustme.di.featureModules
import com.youllbecold.trustme.di.navHostModule
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
            modules(navHostModule + commonModules + featureModules)
        }

        // Create notification channels
        ChannelInitializer.initChannels(this)
    }
}
