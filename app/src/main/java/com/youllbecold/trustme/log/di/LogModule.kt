package com.youllbecold.trustme.log.di

import com.youllbecold.trustme.log.add.ui.AddLogViewModel
import com.youllbecold.trustme.log.edit.ui.EditLogViewModel
import com.youllbecold.trustme.log.history.ui.HistoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val logModule = module {
    viewModelOf(::AddLogViewModel)
    viewModelOf(::EditLogViewModel)
    viewModelOf(::HistoryViewModel)
}
