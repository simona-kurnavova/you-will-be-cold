package com.youllbecold.trustme.log.di

import com.youllbecold.trustme.log.add.ui.AddLogViewModel
import com.youllbecold.trustme.log.edit.ui.EditLogViewModel
import com.youllbecold.trustme.log.history.ui.HistoryViewModel
import com.youllbecold.trustme.log.add.usecases.AddLogUseCase
import com.youllbecold.trustme.log.edit.usecases.EditLogUseCase
import com.youllbecold.trustme.log.edit.usecases.FetchLogUseCase
import com.youllbecold.trustme.log.history.usecases.DeleteLogUseCase
import com.youllbecold.trustme.log.history.usecases.FetchAllLogsUseCase
import com.youllbecold.trustme.log.usecases.ObtainLogWeatherParamsUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val logModule = module {
    viewModelOf(::AddLogViewModel)
    viewModelOf(::EditLogViewModel)
    viewModelOf(::HistoryViewModel)

    factoryOf(::AddLogUseCase)
    factoryOf(::EditLogUseCase)
    factoryOf(::ObtainLogWeatherParamsUseCase)
    factoryOf(::FetchLogUseCase)
    factoryOf(::DeleteLogUseCase)
    factoryOf(::FetchAllLogsUseCase)
}
