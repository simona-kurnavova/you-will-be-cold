package com.youllbecold.trustme.overlays.di

import com.youllbecold.trustme.overlays.locationpermission.ui.LocationPermissionViewModel
import com.youllbecold.trustme.overlays.notificationpermissions.ui.NotificationViewModel
import com.youllbecold.trustme.overlays.welcome.ui.WelcomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val overlayModule = module {
    viewModelOf(::LocationPermissionViewModel)
    viewModelOf(::NotificationViewModel)
    viewModelOf(::WelcomeViewModel)
}