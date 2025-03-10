package com.youllbecold.trustme.settings.di

import com.youllbecold.trustme.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val settingsModule = module {
    viewModelOf(::SettingsViewModel)
}
