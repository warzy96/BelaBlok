package com.hr.warzy.bela.turn

sealed class TurnViewState {
    data class Score(
        val ourScore: Int,
        val yourScore: Int,
        val ourFourJacks: Boolean,
        val ourFourNines: Boolean,
        val ourBelot: Boolean,
        val ourTwenties: Int,
        val ourFifties: Int,
        val ourHundreds: Int,
        val yourFourJacks: Boolean,
        val yourFourNines: Boolean,
        val yourBelot: Boolean,
        val yourTwenties: Int,
        val yourFifties: Int,
        val yourHundreds: Int
    ) : TurnViewState()

    data class OnOurScoreChanged(val yourScore: Int) : TurnViewState()
    data class OnYourScoreChanged(val ourScore: Int) : TurnViewState()
    data class TotalScoreHeader(val ourTotalScore: Int, val yourTotalScore: Int) : TurnViewState()
}