package com.youllbecold.trustme.di

import com.youllbecold.trustme.ui.viewmodels.AddLogViewModel
import com.youllbecold.trustme.ui.viewmodels.EditLogViewModel
import com.youllbecold.trustme.ui.viewmodels.HistoryViewModel
import com.youllbecold.trustme.ui.viewmodels.HomeViewModel
import com.youllbecold.trustme.ui.viewmodels.LocationPermissionViewModel
import com.youllbecold.trustme.ui.viewmodels.MainViewModel
import com.youllbecold.trustme.ui.viewmodels.SettingsViewModel
import com.youllbecold.trustme.ui.viewmodels.WelcomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val uiModule = module {
    viewModelOf(::SettingsViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::HistoryViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::AddLogViewModel)
    viewModelOf(::WelcomeViewModel)
    viewModelOf(::LocationPermissionViewModel)
    viewModelOf(::EditLogViewModel)
}
