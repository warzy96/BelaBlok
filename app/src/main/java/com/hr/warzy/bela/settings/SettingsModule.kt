package com.hr.warzy.bela.settings

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

fun settingsModule(): Module = module {
    viewModel { SettingsViewModel(get(), get(), get(), get()) }
}