package com.youllbecold.trustme.di

import com.youllbecold.trustme.ui.NavHostViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val navHostModule = module {
    viewModelOf(::NavHostViewModel)
}
