package com.hr.warzy.bela.base

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val BACKGROUND_SCHEDULER = "BACKGROUND_SCHEDULER"
const val MAIN_SCHEDULER = "MAIN_SCHEDULER"

fun threadingModule(): Module = module {

    single(named(BACKGROUND_SCHEDULER)) { Schedulers.io() }

    single<Scheduler>(named(MAIN_SCHEDULER)) { AndroidSchedulers.mainThread() }
}