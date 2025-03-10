package com.youllbecold.trustme

import android.app.Application
import com.youllbecold.trustme.common.data.di.dataModule
import com.youllbecold.trustme.common.domain.di.domainModule
import com.youllbecold.trustme.common.ui.notifications.channel.ChannelInitializer
import com.youllbecold.trustme.home.di.homeModule
import com.youllbecold.trustme.log.di.logModule
import com.youllbecold.trustme.di.navHostModule
import com.youllbecold.trustme.overlays.di.overlayModule
import com.youllbecold.trustme.recommend.di.recommendModule
import com.youllbecold.trustme.settings.di.settingsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module

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
            modules(navHostModule +commonModules + featureModules)
        }

        // Create notification channels
        ChannelInitializer.initChannels(this)
    }
}

private val commonModules: List<Module> = listOf(
    dataModule,
    domainModule
)

private val featureModules: List<Module> = listOf(
    homeModule,
    logModule,
    overlayModule,
    recommendModule,
    settingsModule,
)
