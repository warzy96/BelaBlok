package com.hr.warzy.bela.currentgame

sealed class CurrentGameViewState {
    data class Turns(val turns: List<CurrentGameTurnItems>) : CurrentGameViewState()
    data class Header(val ourScore: Int, val yourScore: Int) : CurrentGameViewState()
    data class AnimationProgress(val progress: Float) : CurrentGameViewState()
}