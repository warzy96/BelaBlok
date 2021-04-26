package com.hr.warzy.bela.settings

sealed class SettingsViewState {
    data class EndGameResult(val endGameResult: Int) : SettingsViewState()
}