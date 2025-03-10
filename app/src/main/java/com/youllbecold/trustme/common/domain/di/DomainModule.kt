package com.youllbecold.trustme.common.domain.di

import com.youllbecold.trustme.common.domain.notifications.DailyNotificationsManager
import com.youllbecold.trustme.common.domain.units.UnitsManager
import com.youllbecold.trustme.common.domain.usecases.recommendation.RecommendationUseCase
import com.youllbecold.trustme.common.domain.usecases.weather.CurrentWeatherUseCase
import com.youllbecold.trustme.common.domain.usecases.weather.HourlyWeatherUseCase
import com.youllbecold.trustme.common.domain.usecases.weather.RangedWeatherUseCase
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

    // UseCases
    singleOf(::CurrentWeatherUseCase)
    singleOf(::HourlyWeatherUseCase)
    factoryOf(::RangedWeatherUseCase)
    factoryOf(::RecommendationUseCase)

    // Notifications
    factoryOf(::DailyLogNotification)
    factoryOf(::DailyRecommendNotification)
}
