package com.youllbecold.trustme.recommend.di

import com.youllbecold.trustme.recommend.ui.RecommendViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val recommendModule = module {
    viewModelOf(::RecommendViewModel)
}
