package com.hr.warzy.bela.turn

import com.hr.warzy.bela.base.BACKGROUND_SCHEDULER
import com.hr.warzy.bela.base.MAIN_SCHEDULER
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun turnModule(): Module = module {
    viewModel { (turnId: Long, gameId: Long) ->
        TurnViewModel(
            turnId,
            gameId,
            get(),
            get(),
            get(named(MAIN_SCHEDULER)),
            get(named(BACKGROUND_SCHEDULER)),
            get()
        )
    }
}