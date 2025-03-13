package com.youllbecold.trustme.common.domain.di

import com.youllbecold.trustme.common.domain.notifications.DailyNotificationsManager
import com.youllbecold.trustme.common.domain.units.UnitsManager
import com.youllbecold.trustme.common.domain.weather.CurrentWeatherProvider
import com.youllbecold.trustme.common.domain.weather.HourlyWeatherProvider
import com.youllbecold.trustme.common.domain.weather.RangedWeatherProvider
import com.youllbecold.trustme.common.domain.welcome.WelcomeOverlayManager
import com.youllbecold.trustme.common.ui.notifications.DailyLogNotification
import com.youllbecold.trustme.common.ui.notifications.DailyRecommendNotification
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val domainModule = module {
    // Managers
    singleOf(::DailyNotificationsManager)
    singleOf(::UnitsManager)
    singleOf(::WelcomeOverlayManager)

    // Providers
    factoryOf(::CurrentWeatherProvider)
    factoryOf(::HourlyWeatherProvider)
    factoryOf(::RangedWeatherProvider)

    // Notifications
    factoryOf(::DailyLogNotification)
    factoryOf(::DailyRecommendNotification)
}
