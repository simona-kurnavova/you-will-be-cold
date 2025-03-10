package com.youllbecold.trustme.home.di

import com.youllbecold.trustme.home.ui.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val homeModule = module {
    viewModelOf(::HomeViewModel)
}
