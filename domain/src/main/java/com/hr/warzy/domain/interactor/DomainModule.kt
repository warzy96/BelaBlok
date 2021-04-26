package com.hr.warzy.domain.interactor

import org.koin.dsl.module

fun domainModule() = module {
    single { CalculateTotalScoreUseCase() }
}