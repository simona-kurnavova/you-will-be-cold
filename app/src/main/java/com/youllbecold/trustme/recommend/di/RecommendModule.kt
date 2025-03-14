package com.youllbecold.trustme.recommend.di

import com.youllbecold.trustme.recommend.home.ui.HomeViewModel
import com.youllbecold.trustme.recommend.home.usecases.FetchAllWeatherUseCase
import com.youllbecold.trustme.recommend.ranged.ui.RecommendViewModel
import com.youllbecold.trustme.recommend.ranged.usecase.RecommendForDateUseCase
import com.youllbecold.trustme.recommend.domain.RecommendationProvider
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val recommendModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::RecommendViewModel)

    factoryOf(::RecommendForDateUseCase)
    factoryOf(::RecommendationProvider)
    factoryOf(::FetchAllWeatherUseCase)
}
