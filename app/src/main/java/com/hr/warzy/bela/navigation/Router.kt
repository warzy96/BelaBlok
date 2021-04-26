package com.hr.warzy.bela.navigation

import android.view.View

interface Router {
    fun showGamesScreen()

    fun openNewGameScreen(sharedView: View)

    fun openCurrentGameScreen(gameId: Long)

    fun openNewTurnScreen(gameId: Long, sharedView: View)

    fun openExistingTurnScreen(gameId: Long, turnId: Long)

    fun openSettings()

    fun goBack()
}