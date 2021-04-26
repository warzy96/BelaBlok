package com.hr.warzy.bela.settings

import com.hr.warzy.bela.base.BaseViewModel
import com.hr.warzy.bela.navigation.RoutingActionsDispatcher
import com.hr.warzy.bela.settings.SettingsViewState.EndGameResult
import io.reactivex.Flowable
import io.reactivex.Scheduler

class SettingsViewModel(
    private val settingsSharedPreferences: SettingsSharedPreferences,
    mainThreadScheduler: Scheduler,
    backgroundScheduler: Scheduler,
    routingActionsDispatcher: RoutingActionsDispatcher
) : BaseViewModel<SettingsViewState>(
    mainThreadScheduler = mainThreadScheduler,
    backgroundScheduler = backgroundScheduler,
    routingActionsDispatcher = routingActionsDispatcher
) {

    init {
        query(Flowable.just(settingsSharedPreferences.endGameResult()).map(::EndGameResult))
    }

    fun saveEndGameResult(endGameResult: Int) {
        runCommand(
            settingsSharedPreferences.saveEndGameResult(endGameResult)
        )
    }
}