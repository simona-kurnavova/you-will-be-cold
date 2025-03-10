package com.youllbecold.trustme.di

import com.youllbecold.trustme.common.data.di.dataModule
import com.youllbecold.trustme.common.domain.di.domainModule
import com.youllbecold.trustme.log.di.logModule
import com.youllbecold.trustme.overlays.di.overlayModule
import com.youllbecold.trustme.recommend.di.recommendModule
import com.youllbecold.trustme.settings.di.settingsModule
import org.koin.core.module.Module

/**
 * List of common modules.
 */
internal val commonModules: List<Module> = listOf(
    dataModule,
    domainModule
)

/**
 * List of feature modules.
 */
internal val featureModules: List<Module> = listOf(
    logModule,
    overlayModule,
    recommendModule,
    settingsModule,
)
