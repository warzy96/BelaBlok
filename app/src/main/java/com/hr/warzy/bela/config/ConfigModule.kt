package com.hr.warzy.bela.config

import org.koin.core.module.Module
import org.koin.dsl.module

fun configModule(): Module = module {
    single { TimberAppConfig() }
}